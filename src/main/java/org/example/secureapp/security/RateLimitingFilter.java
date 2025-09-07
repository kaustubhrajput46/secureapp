
package org.example.secureapp.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class RateLimitingFilter implements Filter {

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final long WINDOW_SIZE_SECONDS = 60;

    private final Map<String, RequestWindow> requestWindows = new ConcurrentHashMap<>();

    private static class RequestWindow {
        private int requestCount;
        private long windowStartTime;

        public RequestWindow() {
            this.requestCount = 0;
            this.windowStartTime = Instant.now().getEpochSecond();
        }

        public synchronized boolean allowRequest() {
            long currentTime = Instant.now().getEpochSecond();

            // Reset window if expired
            if (currentTime - windowStartTime >= WINDOW_SIZE_SECONDS) {
                requestCount = 0;
                windowStartTime = currentTime;
            }

            // Check if request can be allowed
            if (requestCount < MAX_REQUESTS_PER_MINUTE) {
                requestCount++;
                return true;
            }

            return false;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Only apply rate limiting to login attempts
        if ("/api/login".equals(httpRequest.getRequestURI()) && "POST".equals(httpRequest.getMethod())) {
            String clientIp = getClientIpAddress(httpRequest);

            RequestWindow window = requestWindows.computeIfAbsent(clientIp, k -> new RequestWindow());

            if (!window.allowRequest()) {
                log.warn("Rate limit exceeded for IP: {}", clientIp);
                httpResponse.setStatus(429); // 429 Too Many Requests
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\":\"Too many login attempts. Please try again later.\"}");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
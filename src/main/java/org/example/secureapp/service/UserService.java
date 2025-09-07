package org.example.secureapp.service;

import org.example.secureapp.dto.RegistrationRequest;
import org.example.secureapp.model.User;
import org.example.secureapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegistrationRequest request) {
        // Input validation and sanitization
        String username = sanitizeInput(request.getUsername());
        String password = request.getPassword();

        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username must be between 3 and 50 characters");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        // Check if user already exists (using parameterized query)
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Hash password with BCrypt
        String hashedPassword = passwordEncoder.encode(password);

        // Create and save user
        User user = new User(username, hashedPassword);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        log.info("New user registered: {}", username);
    }

    private String sanitizeInput(String input) {
        if (input == null) return "";
        // Remove potential SQL injection characters and trim
        return input.trim()
                .replaceAll("[';\"\\\\]", "")
                .replaceAll("(?i)(union|select|insert|update|delete|drop|create|alter)", "");
    }
}

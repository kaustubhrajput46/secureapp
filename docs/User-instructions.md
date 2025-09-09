# SecureApp - Testing Documentation

## 📖 Overview

This document provides comprehensive testing instructions for the SecureApp - a Spring Boot secure web application that demonstrates enterprise-grade security features including HTTPS/SSL, rate limiting, user authentication, and security best practices.

## 🔐 Security Features

- **HTTPS/SSL Encryption** - All communications encrypted with TLS
- **Rate Limiting** - Protection against brute force attacks (10 requests/minute per IP)
- **CSRF Protection** - Cross-Site Request Forgery prevention
- **Security Headers** - HSTS, Content-Type Options, Frame Options
- **Session Management** - Secure session handling with concurrent session control
- **Input Validation** - Server-side validation with Bean Validation
- **SQL Injection Protection** - Parameterized queries with Spring Data JPA

## 🚀 Prerequisites

Before starting the tests, ensure:
- Application is running at https://localhost:8443
- Browser accepts self-signed SSL certificates
- Developer tools are available for header inspection

## 📝 Testing Instructions

### 1. User Registration Test

**Objective:** Verify user registration functionality and input validation

**Steps:**
1. Navigate to https://localhost:8443/login
2. Click "Don't have an account? Register here"
3. Fill registration form:
    - Username: `testuser` (3-50 characters)
    - Password: `TestPass123!` (minimum 8 characters)
4. Click "Register"

**Expected Results:**
- User created successfully
- Redirected to login page with success message
- User data stored in H2 database

**Validation Tests:**
- Username < 3 characters: Should fail
- Username > 50 characters: Should fail
- Password < 8 characters: Should fail
- Duplicate username: Should fail with error message

### 2. Basic Authentication Test

**Objective:** Test user login functionality

**Steps:**
1. Navigate to https://localhost:8443/login
2. Enter credentials:
    - Username: `testuser`
    - Password: `TestPass123!`
3. Click "Login"

**Expected Results:**
- Successful authentication
- Redirected to dashboard page
- Session cookie created (JSESSIONID)
- User marked as authenticated

### 3. Rate Limiting Test (Critical Security Feature)

**Objective:** Verify brute force protection mechanism

**Steps:**
1. Logout if currently logged in
2. Navigate to https://localhost:8443/login
3. Attempt rapid failed logins:
    - Username: `testuser`
    - Password: `wrongpassword`
    - Repeat quickly 11+ times

**Expected Results:**
- First 10 attempts: Normal login failure response
- 11th attempt onwards: HTTP 429 status
- Response body: `{"error":"Too many login attempts. Please try again later."}`
- Rate limit resets after 60 seconds

**Technical Details:**
- Rate limit: 10 requests per minute per IP
- Window: 60-second sliding window
- Scope: Only `/api/login` POST requests
- Logging: IP addresses logged for violations

### 4. HTTPS Security Test

**Objective:** Verify SSL/TLS encryption

**Steps:**
1. Check browser address bar for padlock icon
2. Click padlock to view certificate details
3. Attempt HTTP access: http://localhost:8443/login

**Expected Results:**
- HTTPS enforced on all requests
- Certificate shows "secureapp" alias
- HTTP requests fail or redirect to HTTPS
- TLS encryption active

### 5. Session Management Test

**Objective:** Test concurrent session control

**Steps:**
1. Login successfully in primary browser
2. Open incognito/private browser window
3. Navigate to https://localhost:8443/login
4. Login with same credentials

**Expected Results:**
- First session invalidated
- Only one active session per user
- Previous browser session redirected to login
- Maximum sessions: 1 per user

### 6. CSRF Protection Test

**Objective:** Verify Cross-Site Request Forgery protection

**Steps:**
1. Login to application
2. Open browser Developer Tools (F12)
3. Go to Network tab
4. Submit any form (login/register)
5. Inspect request headers

**Expected Results:**
- CSRF token present in form submissions
- Token in header: `X-XSRF-TOKEN`
- Cookie: `XSRF-TOKEN`
- Requests without token should fail

### 7. Security Headers Test

**Objective:** Verify security headers implementation

**Steps:**
1. Open Developer Tools (F12)
2. Navigate to Network tab
3. Refresh any page
4. Select main document request
5. Check Response Headers

**Expected Headers:**
```
Strict-Transport-Security: max-age=31536000; includeSubDomains
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
Referrer-Policy: strict-origin-when-cross-origin
```


### 8. Logout Functionality Test

**Objective:** Test secure logout process

**Steps:**
1. Login successfully
2. Access dashboard page
3. Initiate logout (POST request)

**Expected Results:**
- Redirected to login page
- Success message: "You have been logged out successfully"
- Session invalidated
- JSESSIONID cookie deleted
- Cannot access protected pages without re-authentication

## 🌐 URL Reference

| URL | Access Level | Purpose |
|-----|-------------|---------|
| `https://localhost:8443/` | Public | Home page |
| `https://localhost:8443/login` | Public | Login form |
| `https://localhost:8443/register` | Public | Registration form |
| `https://localhost:8443/dashboard` | Authenticated | User dashboard |
| `https://localhost:8443/api/register` | Public | Registration API |
| `https://localhost:8443/api/login` | Public | Login API (rate limited) |

## 🚨 Expected Security Behaviors

### Authentication
- ✅ Successful login with valid credentials
- ❌ Failed login with invalid credentials
- 🔒 Account lockout support (infrastructure ready)

### Rate Limiting
- ⚡ 429 HTTP status after 10 login attempts
- ⏱️ 60-second reset window
- 📍 IP-based tracking
- 📝 Violation logging

### Session Security
- 🔐 HTTPS-only sessions
- 👤 Single concurrent session per user
- ⏰ Session timeout on inactivity
- 🗑️ Secure logout with cleanup

### Input Validation
- ✔️ Server-side validation on all inputs
- 🚫 Proper error messages for invalid data
- 🛡️ SQL injection prevention via parameterized queries
- 🔢 Length and format restrictions

## 📊 Monitoring Points

### Application Logs
Monitor console output for:
- User registration events
- Authentication attempts (success/failure)
- Rate limiting violations with IP addresses
- Security configuration loading
- CSRF token validation

### Browser Network Tab
Check for:
- HTTPS requests only
- Security headers presence
- CSRF tokens in forms
- Session cookie handling
- Proper error responses

## 🎯 Key Demonstration Points

1. **Enterprise Security:** HTTPS-only application with proper SSL configuration
2. **Brute Force Protection:** Effective rate limiting prevents automated attacks
3. **Modern Security Standards:** Contemporary security headers and CSRF protection
4. **Session Management:** Secure session handling with concurrent control
5. **Input Validation:** Comprehensive server-side validation prevents malicious input
6. **Audit Trail:** Security events logged for monitoring and analysis

## ⚠️ Important Notes

- **Development Environment:** Self-signed certificate will show browser warnings
- **In-Memory Database:** All data lost on application restart
- **Rate Limiting:** IP-based, so different machines have separate limits
- **Session Timeout:** Sessions expire on browser closure or inactivity
- **Security Headers:** All responses include protective headers

## 🔍 Troubleshooting

### Common Issues
- **SSL Certificate Warning:** Expected for self-signed certificates
- **Rate Limit Not Working:** Ensure testing from same IP address
- **Session Issues:** Check browser cookie settings
- **CSRF Errors:** Verify tokens are included in form submissions

### Reset Instructions
- **Clear Rate Limits:** Wait 60 seconds or restart application
- **Reset Database:** Restart application (H2 in-memory)
- **Clear Sessions:** Close all browser windows or restart app

---


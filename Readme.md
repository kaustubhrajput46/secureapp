# SecureApp - Spring Boot Secure Web Application

A secure web application built with Spring Boot that demonstrates enterprise-grade security features including HTTPS/SSL, rate limiting, user authentication, and security best practices.

## 🔐 Security Features

- **HTTPS/SSL Encryption** - All communications encrypted with TLS
- **Rate Limiting** - Protection against brute force attacks (10 requests/minute per IP)
- **CSRF Protection** - Cross-Site Request Forgery prevention
- **Security Headers** - HSTS, Content-Type Options, Frame Options
- **Session Management** - Secure session handling with concurrent session control
- **Input Validation** - Server-side validation with Bean Validation
- **SQL Injection Protection** - Parameterized queries with Spring Data JPA

## 🚀 Technologies Used

- **Java 17** - Programming language
- **Spring Boot 3.5.5** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database for development
- **Thymeleaf** - Template engine for web pages
- **Lombok** - Reduces boilerplate code
- **Jakarta EE** - Enterprise Java specifications
- **Maven** - Build and dependency management

## 📋 Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **IntelliJ IDEA** (recommended) or any IDE
- **Modern web browser** with HTTPS support

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
git clone <your-repository-url>
cd secureapp
```


### 2. Build the Application
```shell script
mvn clean install
```


### 3. Run the Application
```shell script
mvn spring-boot:run
```


### 4. Access the Application
- **HTTPS URL:** https://localhost:8443
- **Accept the SSL certificate** (self-signed for development)

## 🔑 SSL Certificate

The application uses a self-signed SSL certificate for HTTPS encryption:
- **Keystore:** `classpath:keystore.p12`
- **Password:** `securepass`
- **Alias:** `secureapp`

## 📂 Project Structure

```
src/
├── main/
│   ├── java/org/example/secureapp/
│   │   ├── SecureappApplication.java          # Main application class
│   │   ├── model/                             # Data entities
│   │   │   └── User.java                      # User entity
│   │   ├── repository/                        # Data access layer
│   │   │   └── UserRepository.java            # User repository
│   │   ├── security/                          # Security configuration
│   │   │   ├── RateLimitingFilter.java        # Rate limiting filter
│   │   │   ├── SecurityConfig.java            # Security configuration
│   │   │   └── CustomUserDetailsService.java # User authentication service
│   │   └── service/                           # Business logic
│   │       └── UserService.java               # User management service
│   └── resources/
│       ├── application.properties             # Application configuration
│       ├── keystore.p12                       # SSL certificate
│       └── templates/                         # Thymeleaf templates
│           └── login.html                     # Login page template
```


## 🌐 Endpoints

### Public Endpoints
- `GET /` - Home page
- `GET /login` - Login page
- `GET /register` - Registration page
- `POST /api/register` - User registration
- `POST /api/login` - User authentication (rate limited)

### Protected Endpoints
- `GET /dashboard` - User dashboard (requires authentication)
- `GET /admin/**` - Admin pages (requires ADMIN role)
- `POST /logout` - User logout

## 👤 User Management

### User Registration
Users can register through the registration endpoint with the following requirements:

### Password Requirements
- Minimum 8 characters (enforced by UserService)
- Username must be between 3 and 50 characters
- Username must be unique

### User Entity Features
- Account locking mechanism (accountNonLocked field)
- Failed login attempts tracking
- Last login timestamp
- User roles (default: "USER")

## 🔒 Security Configuration

### Rate Limiting
- **Login endpoints:** 10 requests per minute per IP address
- **Automatic reset:** 60-second sliding window
- **Response:** HTTP 429 (Too Many Requests)

### HTTPS Configuration
```properties
server.port=8443
server.ssl.enabled=true
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password=securepass
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=secureapp
```


### Security Headers
- **HSTS** - HTTP Strict Transport Security with 1 year max-age
- **Frame Options** - DENY to prevent clickjacking
- **Content Type Options** - nosniff to prevent MIME type sniffing
- **Referrer Policy** - STRICT_ORIGIN_WHEN_CROSS_ORIGIN

## 📊 Database Configuration

The application uses H2 in-memory database for development:
```properties
spring.datasource.url=jdbc:h2:mem:securedb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```


## 🔧 Development

### Building for Production
```shell script
# Build JAR file
mvn clean package

# Run the JAR
java -jar target/secureapp-0.0.1-SNAPSHOT.jar
```


## 🚨 Security Best Practices Implemented

1. **HTTPS Everywhere** - All traffic encrypted
2. **Rate Limiting** - Prevents brute force attacks
3. **CSRF Protection** - Prevents cross-site request forgery
4. **Security Headers** - HSTS, X-Content-Type-Options, X-Frame-Options
5. **Session Security** - Secure session management
6. **Input Validation** - Server-side validation of all inputs
7. **SQL Injection Prevention** - Parameterized queries
8. **Password Hashing** - BCrypt with cost factor 12
9. **Account Lockout Support** - Infrastructure for failed login tracking

## 🔍 Monitoring & Logging

- **Security events** logged at INFO level
- **Rate limiting violations** logged with IP addresses
- **Spring Security** debug logging enabled
- **User registration** events logged

## 🚀 Deployment

### Production Considerations
1. **Replace self-signed certificate** with CA-signed certificate
2. **Configure external database** (PostgreSQL, MySQL)
3. **Disable H2 console** (already disabled in application.properties)
4. **Set up proper logging** configuration
5. **Configure load balancing** for high availability

## 📝 License

This project is for educational purposes and demonstrates secure coding practices in Spring Boot applications.

---

**⚠️ Important:** This application uses a self-signed certificate for development. For production use, replace with a proper SSL certificate from a trusted Certificate Authority.


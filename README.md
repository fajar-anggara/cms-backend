# CMS Backend API

Sebuah backend app untuk aplikasi Content Management System (CMS) seperti Wordpress dan blogger untuk management article

## About

Ini merupakan apikasi REST API untuk fitur basic CMS yang saya buat sembari belajar bahasa pemrograman java dan teknologi pendukungnya terutama spring boot. Aplikasi ini akan mengukur pemahaman saya tentang:
- REST API development dengan Spring Boot
- JWT authentication dan authorization untuk keamanan di aplikasi yang stateless 
- API documentation menggunakan Open API / Swagger
- Unit testing menggunakan JUnit
- API-first development approach menggunaka openapi generator

## Features

### Sudah terbangun

- **User Management**: CRUD user
- **Authentication**: JWT access token dan refresh token rotation
- **Authorization**: Akses aplikasi tesedia utuk dua role (SuperUser, Blogger) dan public akan diberi anonymous
- **API Documentation**: Api documentation menggunakan Swagger/Open Api.

### Sedang dikerjakan

- **Content Management**: CRUD post dan Categories dengan sanitasi, menerima markdown sebagai request.
- **UnitTest Coverage**: Sedang meningkatkan coverage unit test dan akan menggunakan Test Driven Development mulai dari sekarang.

## Tech Stack

- **Java 17** - language
- **Spring Boot 3.x** - Framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database access
- **MySQL 8.0** - Database
- **JWT** - Token-based authentication
- **OpenAPI 3.0** - API documentation
- **Lombok** - Boilerplate code reducer
- **Mapstruct** - Mapper
- **Owasp** - Sanitizer
- **Spring-mail** - Mail sender
- **JUnit 5** - Testing
- **Maven** - Build tool


## Project Structure

```
src/
└── main/
    ├── java/
    │   └── com/
    │       └── backendapp/
    │           └── cms/
    │               ├── blogging/
    │               │   ├── dto/
    │               │   ├── endpoint/
    │               │   ├── entity/
    │               │   ├── helper/
    │               │   └── repository/
    │               ├── blogs/
    │               ├── common/
    │               │   ├── config/
    │               │   ├── constant/
    │               │   ├── dto/
    │               │   ├── enums/
    │               │   └── exception/
    │               │       └── handler/
    │               ├── email/
    │               │   └── service/
    │               ├── security/
    │               │   ├── config/
    │               │   ├── dto/
    │               │   ├── endpoint/
    │               │   ├── entity/
    │               │   ├── exception/
    │               │   ├── jwt/
    │               │   ├── repository/
    │               │   ├── service/
    │               │   └── validation/
    │               │       ├── annotation/
    │               │       └── annotationValidator/
    │               ├── superuser/
    │               │   ├── converter/
    │               │   ├── endpoint/
    │               │   └── service/
    │               └── users/
    │                   ├── converter/
    │                   │   └── mapper/
    │                   ├── dto/
    │                   ├── endpoint/
    │                   ├── entity/
    │                   ├── exception/
    │                   ├── helper/
    │                   ├── repository/
    │                   └── service/
    └── resources/
        ├── api-specs/
        ├── static/
        └── templates/
```

## API Documentation

Once running, you can access:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- MySQL 8.0+

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/fajar-anggara/cms-backend
   cd cms-backend
   ```

2. Run the application
   ```bash
   mvn spring-boot:run
   ```

3. Access API documentation at `http://localhost:8080/swagger-ui.html`

## Yang saya pelajari

### Yang saya pelajari saat membuat aplikasi ini:

1. **API-First Development**
    - Menulis API spesification sebelum menulis code
    - Generate interface secara otomatis menggunakan openapi generator untuk di implementasi
    - Code selalu sesuai dengan API spesification karena mengimplementasi interface hasil generated

2. **JWT Iplementation**
    - JWT token generation dan validasi menggunakan sign key
    - Implementasi refresh token rotation
    - Role-based access sesuai endpoint

3. **Kualitas Code**
    - Custom validation annotations
    - Global exception handling
    - Separation Of Concern

## Current Status

This is a work-in-progress learning project. Currently implemented:
- CRUD user
- CRUD user by superuser
- JWT token handling
- Basic API documentation
- Unit tests for user module
- Post management (in development)
- Public reading API (planned)

## Testing

Run tests with:
```bash
mvn test
```

Test coverage sekarang:
- Helper layer dari Post Article

## What's Next

Saya akan membuat aplikasi ini scalable dan kedepannya akan:
- Memperbaiki test coverage
- Menyelesaikan POST managment
- Fitur komentar
- OAUTH dengan auhtentication server yang terpisah (akan menggunakan Laravel)
- Menambahkan custom SEO
- Caching

## Notes

Aplikasi ini bukan production-ready, saya membuat ini untuk belajar backend developer. Aplikasi ini akan menemani saya mempelajari konsep konsep di dunia teknologi Development menggunkaan spring boot dan mengimplementasikannya. 
Saya berharap suatu hari aplikasi ini bisa industrial-grade dan akan terus ada sejauh pemahaman saya tentang programming meningkat.

## Contact

- **Email**: muhamadfajaranggara@gmail.com
- **LinkedIn**: https://www.linkedin.com/in/moh-fajar-anggara-252219180/

---

*Aplikasi ini adalah bagian dari belajar saya untuk menjadi backend developer dan saya menampilkan ini untuk menjadikannya sebagai portofolio dan memperlihatkan apa saja yang saya kuasai.*
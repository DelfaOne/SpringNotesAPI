# Spring Boot REST API

A secure REST API built with Spring Boot and Kotlin for managing notes with user authentication.

## 🚀 Features

- **User Authentication**: Register, login, and refresh token functionality
- **JWT Security**: Secure API endpoints with JWT tokens
- **Note Management**: Create, read, update, and delete notes
- **MongoDB Integration**: Reactive MongoDB support for data persistence
- **Input Validation**: Request validation with proper error handling
- **Security**: Password encryption and secure token management

## 🛠 Tech Stack

- **Language**: Kotlin
- **Framework**: Spring Boot 3.5.3
- **Security**: Spring Security with JWT
- **Database**: MongoDB (Reactive)
- **Build Tool**: Gradle (Kotlin DSL)
- **Java Version**: 21

## 📋 Prerequisites

- Java 21 or higher
- MongoDB instance
- Gradle (included via wrapper)

## ⚙️ Environment Variables

Create a `.env` file or set the following environment variables:

```bash
MONGO_DB_KEY=mongodb://localhost:27017/your-database-name
JWT_SECRET_BASE64=your-base64-encoded-jwt-secret
```

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone <repository-url>
cd spring_boot_rest_api
```

### 2. Set up environment variables
Make sure MongoDB is running and set the required environment variables.

### 3. Build and run the application
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The API will be available at `http://localhost:8085`

## 📚 API Documentation

### Authentication Endpoints

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePassword"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "securePassword"
}
```

**Response:**
```json
{
  "accessToken": "jwt-access-token",
  "refreshToken": "jwt-refresh-token"
}
```

#### Refresh Token
```http
POST /auth/refresh
Content-Type: application/json

{
  "refreshToken": "your-refresh-token"
}
```

### Notes Endpoints

All note endpoints require authentication via Bearer token.

#### Create Note
```http
POST /notes
Authorization: Bearer <access-token>
Content-Type: application/json

{
  "title": "Note Title",
  "content": "Note content"
}
```

#### Get Notes
```http
GET /notes?ownerId=<user-id>
Authorization: Bearer <access-token>
```

#### Delete Note
```http
DELETE /notes/{noteId}
Authorization: Bearer <access-token>
```

## 🏗 Project Structure

```
src/
├── main/
│   ├── kotlin/com/delfa/spring_boot_rest_api/
│   │   ├── SpringBootRestApiApplication.kt
│   │   ├── GlobalValidationHandler.kt
│   │   ├── controllers/
│   │   │   ├── auth/
│   │   │   │   ├── AuthController.kt
│   │   │   │   └── model/
│   │   │   └── note/
│   │   │       ├── NoteController.kt
│   │   │       └── model/
│   │   ├── database/
│   │   │   ├── model/
│   │   │   └── repository/
│   │   └── security/
│   └── resources/
│       └── application.properties
└── test/
```

## 🔧 Configuration

The application uses the following configuration in `application.properties`:

- **Server Port**: 8085
- **MongoDB**: Configured via `MONGO_DB_KEY` environment variable
- **JWT Secret**: Configured via `JWT_SECRET_BASE64` environment variable
- **Auto-index Creation**: Enabled for MongoDB

## 📝 Dependencies

Key dependencies include:
- Spring Boot Web
- Spring Boot Security
- Spring Data MongoDB (Reactive)
- JWT (JSON Web Tokens)
- Kotlin Coroutines
- Bean Validation

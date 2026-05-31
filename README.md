# 🎬 CineLog — Movie Tracker App

CineLog is a full-stack movie tracking web application that allows users to manage their personal movie list — add, update, delete, and track watching status.

---

## 🏛️ Overall Architecture

```mermaid
flowchart LR
	subgraph Client
		Web[Static HTML/CSS/JS]
	end

	subgraph Backend[Spring Boot App]
		Auth[AuthController]
		Movies[MovieController]
		MovieSvc[MovieService]
		MailSvc[EmailService]
		ResetMailSvc[ResetPasswordEmailService]
		UserRepo[UserRepository]
		MovieRepo[MovieRepository]
		TokenRepo[VerificationTokenRepository]
	end

	subgraph Data
		DB[(MySQL Database)]
		Mail[(SMTP Mail Server)]
	end

	Web --> Auth
	Web --> Movies

	Auth --> UserRepo --> DB
	Auth --> TokenRepo --> DB
	Auth --> MailSvc --> Mail
	Auth --> ResetMailSvc --> Mail

	Movies --> MovieSvc --> MovieRepo --> DB
	Movies --> UserRepo
```

## 🧭 Sequence Diagram: Sign Up + Email Verification

```mermaid
sequenceDiagram
	actor Client
	participant AuthController
	participant UserRepository
	participant VerificationTokenRepository
	participant EmailService
	participant MailServer

	Client->>AuthController: POST /auth/signup (name,email,password)
	AuthController->>UserRepository: save(User enabled=false)
	AuthController->>VerificationTokenRepository: save(VerificationToken token, user, expiry=now+24h)
	AuthController->>EmailService: sendVerificationMail(to, token, subject, text)
	EmailService->>MailServer: send email with /auth/verify?token=...

	Client->>AuthController: GET /auth/verify?token=...
	AuthController->>VerificationTokenRepository: findByToken(token)
	alt token expired
		AuthController-->>Client: 400 "Token expired"
	else token valid
		AuthController->>UserRepository: save(User enabled=true)
		AuthController->>VerificationTokenRepository: delete(token)
		AuthController-->>Client: 200 "Email verified successfully"
	end
```

## 🧩 Sequence Diagram: All Features

```mermaid
sequenceDiagram
	actor Client
	participant AuthController
	participant MovieController
	participant MovieService
	participant UserRepository
	participant MovieRepository
	participant VerificationTokenRepository
	participant EmailService
	participant ResetPasswordEmailService
	participant MailServer
	participant SessionStore

	%% Sign up + verify
	Client->>AuthController: POST /auth/signup (name,email,password)
	AuthController->>UserRepository: save(User enabled=false)
	AuthController->>VerificationTokenRepository: save(token, user, expiry)
	AuthController->>EmailService: sendVerificationMail(to, token, subject, text)
	EmailService->>MailServer: send email with /auth/verify?token=...

	Client->>AuthController: GET /auth/verify?token=...
	AuthController->>VerificationTokenRepository: findByToken(token)
	alt token expired
		AuthController-->>Client: 400 "Token expired"
	else token valid
		AuthController->>UserRepository: save(User enabled=true)
		AuthController->>VerificationTokenRepository: delete(token)
		AuthController-->>Client: 200 "Email verified successfully"
	end

	%% Login + logout
	Client->>AuthController: POST /auth/login (email,password)
	AuthController->>UserRepository: findByEmail(email)
	alt invalid email or password
		AuthController-->>Client: 401 "Invalid email/password"
	else not verified
		AuthController-->>Client: 400 "Please verify your email first."
	else success
		AuthController->>SessionStore: set userId
		AuthController-->>Client: 200 "Login successful"
	end

	Client->>AuthController: POST /auth/logout
	AuthController->>SessionStore: invalidate
	AuthController-->>Client: 200 "Logged out"

	%% Forgot + reset password
	Client->>AuthController: POST /auth/forgot-password (email)
	AuthController->>UserRepository: findByEmail(email)
	alt email not found
		AuthController-->>Client: 401 "Invalid Email"
	else email found
		AuthController->>VerificationTokenRepository: save(token, user, expiry)
		AuthController->>ResetPasswordEmailService: sendVerificationMail(to, token, subject, text)
		ResetPasswordEmailService->>MailServer: send email with reset link
		AuthController-->>Client: 200 "Email has been sent."
	end

	Client->>AuthController: PATCH /auth/reset-password (token,newPassword)
	AuthController->>VerificationTokenRepository: findByToken(token)
	alt token expired
		AuthController-->>Client: 400 "Token expired"
	else token valid
		AuthController->>UserRepository: save(User password=newPassword)
		AuthController->>VerificationTokenRepository: delete(token)
		AuthController-->>Client: 200 "Password reset successfully"
	end

	%% Movies CRUD (requires session)
	Client->>MovieController: GET /movies
	MovieController->>SessionStore: get userId
	MovieController->>UserRepository: findById(userId)
	MovieController->>MovieService: getAllMovies(user, pageable)
	MovieService->>MovieRepository: findByUser(user, pageable)
	MovieController-->>Client: 200 Page<Movie>

	Client->>MovieController: POST /movies (movie)
	MovieController->>SessionStore: get userId
	MovieController->>UserRepository: findById(userId)
	MovieController->>MovieService: addMovie(movie with user)
	MovieService->>MovieRepository: save(movie)
	MovieController-->>Client: 200 Movie

	Client->>MovieController: PUT /movies/{id} (movie)
	MovieController->>SessionStore: get userId
	MovieController->>UserRepository: findById(userId)
	MovieController->>MovieService: getById(id)
	MovieService->>MovieRepository: getById(id)
	alt not owner
		MovieController-->>Client: 403 "Forbidden"
	else owner
		MovieController->>MovieService: addMovie(updated)
		MovieService->>MovieRepository: save(movie)
		MovieController-->>Client: 200 "Updated"
	end

	Client->>MovieController: DELETE /movies/{id}
	MovieController->>SessionStore: get userId
	MovieController->>UserRepository: findById(userId)
	MovieController->>MovieService: getById(id)
	MovieService->>MovieRepository: getById(id)
	alt not owner
		MovieController-->>Client: 403 "Forbidden"
	else owner
		MovieController->>MovieService: delete(id)
		MovieService->>MovieRepository: deleteById(id)
		MovieController-->>Client: 200 "Deleted"
	end
```

## 🚀 Features

- 🔐 Session-based authentication  
- 🎥 Add, edit, delete movies  
- 📄 Pagination support  
- 🔍 Search & filter (by status and genre)  
- 📊 Dashboard stats (Total, Watching, Watched)  
- 🛡️ User-based authorization (ownership checks)  
- 🎨 Modern responsive UI  
- 📧 Email verification for registration
- 🕒 Forget password functionality with email reset link

---

## 🏗️ Tech Stack

### Backend
- Java Spring Boot  
- Spring Data JPA  
- Hibernate  
- MySQL (configurable)  
- HttpSession (for authentication)  
- JavaMailSender (for email verification)

### Frontend
- HTML5  
- CSS3 (custom styling)  
- Vanilla JavaScript  

---

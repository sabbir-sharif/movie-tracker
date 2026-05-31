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

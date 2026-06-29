# AI App Builder

An AI-powered development platform that enables users to generate, manage, and preview full-stack applications using natural language instructions.

---

## 🚀 Features

### Authentication
- Login
- Signup
- Get My Profile

### Projects
- Create Project
- Manage Project
- List Projects

### AI Code Generation
- List Chat Sessions
- Create New Chat Session
- Load Full Chat History
- Chat Stream
- Retry Failed Generation

### Files
- Get File Tree & Metadata
- Get File Content
- Download All Files as ZIP

### Preview
- Get Project Preview
- Live Logs Stream
- Preview Status Tracking
- Stop/Delete Preview

### Members & Collaboration
- Invite Members
- Change Member Roles
- Remove Members
- Multi-user Project Access

### Subscription & Billing
- FREE & PRO Plans
- Stripe Checkout
- Customer Billing Portal
- Usage & Quota Tracking

### Additional Features
- Token Usage Quota
- Preview Running Quota
- Rate Limiting
- Zipkin Tracing

---

## 🛠️ Tech Stack

### Backend
- Java
- Spring Boot
- Spring Security
- Hibernate / JPA

### Database
- PostgreSQL

### DevOps & Cloud
- Docker
- GitHub Actions
- AWS

### Payments
- Stripe

### Frontend (Planned)
- React
- Tailwind CSS

---

## 📡 APIs

### Auth APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login with email/password |
| POST | `/api/auth/signup` | Register new user |
| POST | `/api/auth/refresh` | Refresh access token |
| GET | `/api/auth/profile` | Get current user profile |

### Project APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/project` | List all user projects |
| GET | `/project/{id}` | Get project by ID |
| POST | `/project` | Create new project |
| PATCH | `/project/{id}` | Update project |
| DELETE | `/project/{id}` | Soft delete project |

### File APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/projects/{projectId}/files` | Get file tree |
| GET | `/api/projects/{projectId}/files/{*path}` | Get file content by path |

### Members APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/projects/{projectId}/members` | List project members |
| POST | `/api/projects/{projectId}/members` | Invite a member |
| PATCH | `/api/projects/{projectId}/members/{memberId}` | Update member role |
| DELETE | `/api/projects/{projectId}/members/{memberId}` | Remove member |
| PATCH | `/api/projects/{projectId}/members/accept` | Accept invite |

### Chat APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/chat/stream` | SSE stream AI code generation |
| GET | `/projects/{projectId}` | Get project chat history |

### Billing & Subscription APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/plans` | List all plans |
| GET | `/api/me/subscription` | Get current subscription |
| POST | `/api/payments/checkout` | Create Stripe checkout session |
| POST | `/api/payments/portal` | Open Stripe customer portal |
| POST | `/api/webhooks/payments` | Stripe webhook handler |

### Usage APIs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/usage/today` | Get today's token usage |
| GET | `/api/usage/limits` | Get current plan limits |

---

## 📌 Future Scope

- AI Prompt Engine
- Multi-language Support
- Team Collaboration
- Cloud Deployment

---

## ⚙️ Setup

### Prerequisites
- Java 17
- PostgreSQL
- Docker (optional, for containerized DB)
- Stripe CLI (for webhook testing)

### Environment Variables
Create a `.env` file in the project root:

```
DB_USERNAME=your_db_user
DB_PASSWORD=your_db_password
JWT_SECRET_KEY=your_jwt_secret_min_32_chars
STRIPE_SECRET_KEY=sk_test_...
STRIPE_WEBHOOK_SECRET=whsec_...
```

### Run

```bash
./mvnw clean compile
./mvnw spring-boot:run
```

### Stripe Webhooks (local testing)

```bash
stripe listen --forward-to http://localhost:8080/api/webhooks/payments
```

---

## 🔐 Authorization

Role-based access control with permission mapping:

| Role | Permissions |
|------|-------------|
| OWNER | VIEW, EDIT, EDIT_FILES, DELETE, MANAGE_MEMBERS, VIEW_MEMBERS |
| EDITOR | VIEW, VIEW_MEMBERS, EDIT_FILES |
| VIEWER | VIEW, VIEW_MEMBERS |

---

## 🏗️ Architecture Notes

- JWT-based stateless authentication (access + refresh tokens)
- Role-Permission matrix for fine-grained authorization
- Stripe webhooks for subscription lifecycle management
- MapStruct for DTO mapping
- Global exception handling with structured error responses
- Environment variables externalized via spring-dotenv

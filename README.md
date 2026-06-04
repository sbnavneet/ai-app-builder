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
| Method | Endpoint |
|---|---|
| POST | `/api/auth/login` |
| POST | `/api/auth/signup` |
| GET | `/api/auth/me` |

### Project APIs
| Method | Endpoint |
|---|---|
| CRUD | `/api/projects/{id}` |
| GET | `/api/projects` |

### File APIs
| Method | Endpoint |
|---|---|
| GET | `/api/projects/{id}/files` |
| GET | `/api/projects/{id}/files/**` |
| GET | `/api/projects/{id}/download-zip` |

### Members APIs
| Method | Endpoint |
|---|---|
| GET | `/api/projects/{id}/members` |
| POST | `/api/projects/{id}/members` |
| PATCH | `/api/projects/{id}/members/{userId}` |
| DELETE | `/api/projects/{id}/members/{userId}` |

### Subscription APIs
| Method | Endpoint |
|---|---|
| GET | `/api/plans` |
| GET | `/api/me/subscription` |

### Stripe APIs
| Method | Endpoint |
|---|---|
| POST | `/api/stripe/checkout` |
| POST | `/api/stripe/portal` |

### Usage APIs
| Method | Endpoint |
|---|---|
| GET | `/api/usage/today` |
| GET | `/api/usage/limits` |

### Chat APIs
| Method | Endpoint |
|---|---|
| GET | `/api/projects/{id}/chat-sessions` |
| POST | `/api/projects/{id}/chat-sessions` |
| GET | `/api/chat/sessions/{sessionId}/messages` |
| POST | `/api/chat/stream` |

### Preview APIs
| Method | Endpoint |
|---|---|
| POST | `/api/projects/{id}/preview` |
| GET | `/api/previews/{previewId}/status` |
| SSE | `/api/previews/{previewId}/logs` |
| DELETE | `/api/previews/{previewId}` |

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
DB_URL=jdbc:postgresql://localhost:9000/ai_app_builder_db
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

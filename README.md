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

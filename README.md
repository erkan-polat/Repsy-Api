# Repsy API 📦

A minimalistic REST API for managing and distributing `.rep` software packages, designed for the fictional **Repsy** programming language.

This API allows users to:
- Upload `.rep` packages and their metadata
- Download previously uploaded packages
- Store files using either local filesystem or MinIO (object storage)

---

## 🚀 Technologies

- Java 17
- Spring Boot 3.4.4
- PostgreSQL
- MinIO (Object Storage)
- Docker + Docker Compose

---

## 📦 Project Structure

```bash
repsy-api/
├── src/
│   ├── main/
│   │   ├── java/com/repsy/...
│   │   └── resources/application.properties
├── Dockerfile
├── docker-compose.yml
└── README.md

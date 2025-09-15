# Service-Booking-System
Service Booking System lets customers book home services (e.g., cleaning). Bookings auto-assign a cleaner who visits and completes the service; customers pay through the platform afterwards. An admin dashboard provides real-time tracking of bookings, service status, payments, and cleaner assignments for transparency and operational efficiency.

# Backend Guide

1. Configure Environment Variables

- All required configuration is stored in a .env file at the project root and inside backend folder.
- Copy the example file and update it with your values
- cp .env-example .env
- cp backend/.env-example backend/.env

2. Running with Docker

- The backend uses Docker for consistent setup. Make sure you have:
- Docker installed
- Docker Compose installed

* Step 1: Build the backend image
- docker compose build --no-cache

* Step 2: Start containers
- docker-compose up
- This will start: PostgreSQL, Spring Boot Backend

* Step 3: Verify services
- Backend - http://localhost:8080 (OR port you specify in .env file for docker)
- PostgreSQL - running on localhost:5432 (OR port you specify in .env file for docker)

* Step 4: Stop containers
- docker compose down

*When you set up this project for the first time and run it, a dummy admin account is created with the following credentials:
- Email: admin@example.com
- Phone: 9999999999
- Password: admin123

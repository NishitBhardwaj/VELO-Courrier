# 🚲 VELO Courrier

Welcome to the **VELO Courrier** repository! This is a comprehensive, cross-platform logistics application designed to facilitate seamless delivery, transportation, and courier services. It provides a full-suite ecosystem for customers, drivers, businesses, and administrators.

---

## 🏗️ System Architecture

VELO Courrier is composed of 5 primary high-level modules:

1. **Core System (Backend)**
   - The central brain of the platform.
   - Handles everything from Auth, Role Routing, Booking Lifecycle, Driver Matching (Dispatch algorithms), Payment Processing, to multi-channel Notifications (Push, SMS, Email, WebSocket).
2. **Customer App (Mobile)**
   - For end-users to book instant transport, courier services, or packers & movers.
   - Features include live tracking, fare estimation, dynamic pricing, rating/reviews, and multiple payment methods.
3. **Driver App (Mobile)**
   - Used by delivery partners.
   - Handles Driver KYC, Vehicle Registration, Availability Toggles, Booking request handling (30s countdown), turn-by-turn navigation, and earnings wallet.
4. **Business Dashboard (Web - Planned/WIP)**
   - Portal for B2B clients.
   - Features team user management, bulk CSV bookings, scheduled/recurring routes, and monthly invoicing.
5. **Admin Panel (Web - Planned/WIP)**
   - For internal operations/admin team.
   - Handles KYC approvals, dispute resolution, pricing engine configuration (surge rules), promo management, and booking monitoring.

---

## 📦 Repository Structure

The monorepo contains the following directories:

- `/backend` - The Spring Boot modular monolith backend.
- `/mobile-app` - The Expo / React Native mobile application (Customer & Driver apps).
- `/admin-dashboard` - The frontend application for the admin portal.
- `/flowcharts` - Detailed High-Level Design (HLD) flowcharts in HTML format documenting 42 different system workflows.
- `/diagrams` - Additional system architecture diagrams.
- `/tests` - System testing and integration test suites.

---

## 🛠️ Tech Stack

### Backend (`/backend`)
- **Framework:** Spring Boot 3.2 (Java 21)
- **Database:** PostgreSQL (with Flyway for migrations)
- **Caching & Real-time:** Redis & DB-backed WebSockets for live driver tracking.
- **Security:** Spring Security + JWT, Bucket4j for API Rate Limiting.
- **Observability:** Actuator + Micrometer (Prometheus)
- **API Documentation:** Springdoc OpenAPI (Swagger)

### Mobile App (`/mobile-app`)
- **Framework:** React Native + Expo (SDK 55)
- **Language:** TypeScript
- **Networking:** Axios API, StompJS (WebSockets over STOMP)
- **State & Storage:** React Context, Expo Secure Store

---

## 🚀 Getting Started

### 1. Backend Setup
```bash
cd backend
# Make sure you have PostgreSQL running. Update application properties if needed.
./mvnw clean install
./mvnw spring-boot:run
```
*The backend API will run on `http://localhost:8080` and Swagger UI is automatically generated.*

### 2. Mobile App Setup
```bash
cd mobile-app
# Install dependencies
npm install
# Start the Expo development server
npx expo start
```
*You can press `a` to open in an Android Emulator, `i` to open in an iOS Simulator, or scan the QR code with the Expo Go app on your physical device.*

### 3. Viewing Architecture Documentation
Open `flowcharts/index.html` in your browser to explore the 42 interactive High-Level Design (HLD) flowcharts that detail every interaction in the system.

---

## 📜 License & Contribution
*Currently an internal project.* Please ensure you follow the branch naming conventions (`feature/`, `bugfix/`) and submit PRs for code review. Ensure all backend tests pass via Testcontainers before merging!
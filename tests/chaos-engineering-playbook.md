# Sprint 9: Chaos Engineering & Security Penetration Playbook

To validate VELO Courrier's production survivability, execute the following destructive test suites:

## 1. Chaos Engineering: Component Destruction
**A. Redis Memory Wipe**
- **Action:** Execute `docker stop redis` mid-dispatch.
- **Expected Outcome:** Driver polling endpoints successfully catch `RedisConnectionFailureException`. The application degrades gracefully by temporarily writing Driver Coordinates directly to Postgres or dropping packets silently without crashing the main Booking JVM thread.
- **Pass Criteria:** ZERO 500 Internals. Only localized 503s on `Cache-Aside` elements.

**B. External Maps Routing API Outage**
- **Action:** Alter Mapbox API Key to an invalid string causing `HTTP 401 Unauthorized` responses in the ETA Predictor.
- **Expected Outcome:** The Sprint 7 `Resilience4j` `@CircuitBreaker` instantly trips open after 3 consecutive failures. Future ETA recalculation calls immediately route to the `cacheFallback`, preserving system throughput.
- **Pass Criteria:** `PredictiveEngineService` logs warnings but does not block the active Event Outbox publishers.

**C. Partial Postgres Slowdown**
- **Action:** Inject a random 10-second `pg_sleep` across 30% of read queries.
- **Expected Outcome:** Asynchronous UI loaders spin longer, but parallel booking creation endpoints remain fully isolated via their dedicated HikariCP connection pools.

## 2. Real-Device Physical Profiling
**A. Network Thrashing (4G to Subway Edge/Offline)**
- **Action:** A Driver enters an active booking context, drives into an underground tunnel dropping packets.
- **Expected Outcome:** The React Native payload queues all Proof-of-Delivery notes via SQLite locally. Upon emerging back into 5G, the background queue silently syncs standard `v2` payload updates without the Driver needing to refresh the app.

**B. Battery & Idle Optimization**
- **Action:** Driver application is backgrounded.
- **Expected Outcome:** `expo-location` drops sample-rates algorithmically (e.g. from 1 ping/sec to 1 ping/15secs) to prevent `System Warn: Battery Drain`. Location Socket connection maintains alive.

## 3. Security Penetration Defense
**A. Insecure Direct Object Reference (IDOR)**
- **Action:** User A attempts to `GET /api/v1/bookings/{USER_B_ID}`.
- **Expected Outcome:** Spring Security filters explicitly check if the JWT User ID `==` the Booking Owner ID. Immediate `HTTP 403 Forbidden` response.

**B. JWT State Tampering**
- **Action:** Actor alters the base64-encoded `ROLE_USER` block in their token manually attempting to force `ROLE_ADMIN`.
- **Expected Outcome:** The cryptographic signature verification fails natively upstream in the API Gateway / Spring Interceptor yielding `HTTP 401 Unauthorized`.

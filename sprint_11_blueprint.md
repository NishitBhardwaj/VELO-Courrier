# Sprint 11: AI Optimization Layer
**Platform Architecture Blueprint: Transitioning to Adaptive Machine Learning**

---

## 1. System Architecture
**The Polyglot Shift:**
We are breaking out of the strict Java Spring Boot monolith to introduce `ai-service`, a dedicated Python FastAPI microservice.
*   **Java Backend:** Core truth, transactions, security, and websockets.
*   **Python AI Service:** Stateless mathematical inferences utilizing `scikit-learn` and `XGBoost`.

**Communication Layer:**
Java explicitly calls Python via standard REST/JSON over internal networking. Strict HTTP timeouts limit AI processing delays (e.g., if AI doesn't reply in 400ms -> Java falls back to standard geospatial algorithms natively).

---

## 2. Data Pipelines (Step 1)
**V12 Database Additions (Postgres):**
```sql
CREATE TABLE ml_training_data (
    booking_id UUID PRIMARY KEY,
    pickup_zone VARCHAR(100),
    drop_zone VARCHAR(100),
    time_of_day TIME,
    driver_assigned UUID,
    eta_seconds INT,
    actual_time_seconds INT,
    delay_seconds INT,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE demand_predictions (
    id UUID PRIMARY KEY,
    zone_id UUID NOT NULL,
    predicted_demand INT NOT NULL,
    time_window TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE dispatch_scores (
    id UUID PRIMARY KEY,
    booking_id UUID NOT NULL,
    driver_id UUID NOT NULL,
    score DECIMAL(4,3) NOT NULL,
    model_version VARCHAR(50),
    created_at TIMESTAMP DEFAULT now()
);
```

---

## 3. Model Strategies (Phase 1 Baseline)

### A. Demand Prediction (Foundation)
*   **Inputs:** Historical array from `ml_training_data`, hour of day, day of week.
*   **Algorithm:** `RandomForestRegressor` predicting volume aggregates.
*   **Output:** `{"zoneId": "Z1", "predictedDemand": 45, "surgeProbability": 0.85}`

### B. Smart Dispatch Engine (Core Impact)
*   **Inputs:** Current Driver GPS distance, Driver Rating, Zone constraints, Traffic vectors.
*   **Algorithm:** Normalized weighted scoring function `(0.0 to 1.0)`. Moving away from strictly the *closest* driver towards the *most efficiently positioned* driver.
*   **Output:** Recommended ordered array of Drivers.

### C. Route Optimization (Multi-Stop)
*   **Inputs:** `[[Lat1, Lng1], [Lat2, Lng2], [Lat3, Lng3]]`
*   **Algorithm:** Traveling Salesperson Problem (TSP) heuristic resolving optimal stop routing minimizing Cartesian/Haversine bounds.
*   **Output:** `{"optimized_sequence": [1, 3, 2]}`

---

## 4. API & Feature Flags (Governance)
**Endpoints:**
`GET /ai/demand?zone=Z1&time=18:00`
`POST /ai/dispatch/recommend`
`POST /ai/route/optimize`

**Strict Governance Constraints:**
```json
{
  "feature_flags": {
     "AI_DISPATCH_ENABLED": true,
     "AI_ROUTE_OPTIMIZER_ENABLED": false
  }
}
```
If Python crashes (`HTTP 500` or Timeouts), Java's `CandidateSearchService.java` explicitly ignores the AI and utilizes Redis GEO radius search natively.

---

## 5. Implementation Sequence
1.  **V12 Migration:** Bind the metric tables.
2.  **`ai-service` Scaffolding:** FastAPI + Uvicorn server boot up.
3.  **Model Scaffolding:** Blank ML functions returning heuristic approximations before gathering enough dataset mass for deep learning.
4.  **Java Client:** Spin up `AiServiceClient.java` bridging the Microservices.
5.  **Admin UI:** Visualizing the AI logs in React Dashboards.

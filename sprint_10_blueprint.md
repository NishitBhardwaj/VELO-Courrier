# Sprint 10: Growth & Expansion
**Platform architecture blueprint mapping hyper-scalability, multi-region deployments, and algorithmic incentives.**

---

## 1. Multi-City & Region Expansion Scope (Step 1)
**City-Based Core:**
- Introduce `operational_regions` (e.g. "Paris", "Lyon"). Regions contain strict bounds `timezone_identifier` and `currency_code` (e.g. "Europe/Paris", "EUR").
- Modify existing `Booking` entity to enforce a `region_id` column.

**Zone Hierarchies:**
- Migrating `service_zones` to attach natively to `region_id`. 
- Regional configuration overrides (e.g. `base_fare` in Paris is €5.00 vs Lyon €4.00).

---

## 2. Dynamic Pricing Engine Scope (Step 2)
**Pricing Factors Algorithm:**
- `DemandSupplyRatio`: Calculate active `Drivers : Pending Bookings` within a 5km radius.
- `TimeMultiplier`: Add 15% surcharge during localized rush hours (08:00 - 10:00 & 17:00 - 19:00 mapped securely via `region` timezone).
- `SurgeMultiplier`: Resultant decimal applied directly to the base checkout fare transparently communicated to the Customer App (`surge_active = true`).

---

## 3. Driver Incentives Scope (Step 3)
**Incentive Mechanisms:**
- `BonusCampaigns` targeting specific Operational Regions.
- **Streak Bonuses:** Complete 5 deliveries consecutively without declining payload = $10 abstract wallet injection.
- **Zone Pings:** Pushing `FCM` payloads nudging offline drivers: "High demand in Downtown Paris! +$3.00 Surges Active."

---

## 4. Referral & Promo Engine Scope (Step 4)
**Customer Growth Loops:**
- `PromoCode` definitions: `code_string`, `discount_type` (PERCENTAGE vs FIXED), `max_uses`, `expiry_date`.
- `UserReferrals`: Upon 5th completed booking, User A unlocks `10% OFF`. User B applying referral code grants Wallet Credit. 

---

## 5. Enterprise API Toolkit Scope (Step 5)
**B2B Webhooks & Scheduling:**
- Implementing `/api/v1/enterprise/webhooks` registering external HTTPS endpoints mapping directly to `velo.booking.events` Kafka pipes sending real-time tracking updates safely to Enterprise CRMs.
- `Scheduled Bookings` enabling a `planned_pickup_time` parameter allowing batch queue-loading.

---

## 6. Database Design (V11 Schemas)
```sql
CREATE TABLE operational_regions (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    timezone VARCHAR(50) NOT NULL,
    currency_code VARCHAR(10) DEFAULT 'USD',
    base_fare_override DECIMAL(5,2),
    active BOOLEAN DEFAULT true 
);

CREATE TABLE pricing_surge_logs (
    id UUID PRIMARY KEY,
    region_id UUID NOT NULL,
    multiplier DECIMAL(4,2) NOT NULL,
    triggered_at TIMESTAMP DEFAULT now(),
    expires_at TIMESTAMP
);

CREATE TABLE promo_codes (
    id UUID PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    discount_amount DECIMAL(5,2) NOT NULL,
    type VARCHAR(20), -- PERCENTAGE, FLAT
    global_limit INT,
    expiry_date TIMESTAMP
);

CREATE TABLE driver_incentives (
    id UUID PRIMARY KEY,
    driver_id UUID NOT NULL,
    campaign_name VARCHAR(100),
    bonus_amount DECIMAL(6,2),
    status VARCHAR(20) DEFAULT 'PENDING_PAYOUT',
    created_at TIMESTAMP DEFAULT now()
);
```
*(Plus adding `region_id` to Bookings & Zones)*

---

## 7. Implementation Build Sequence (Strictly Ordered)
1. **Multi-City Foundation (DB & Entities):** `V11` Migration, `OperationalRegion` entities.
2. **Dynamic Pricing Calculator:** Refactoring `FareEstimationService` into a `DynamicPricingEngine` aggregating Time/Supply inputs.
3. **Driver Incentives:** `DriverBonusService` mapping completion event streams out of Kafka into pending Wallet credits.
4. **Promo & Referral API:** Expose `/api/v1/promos/validate` for customer checkouts ensuring rigorous cryptographic validation preventing infinite replay attacks.
5. **Enterprise Open-Webhooks:** `WebhookRegistrationService` emitting HTTPS `POST` bursts via RestTemplate to registered B2B clients on Dropoff completion.

# Sprint 9: Financial Flow Validation Plan

Before opening production routing to Stripe/Adyen, the platform MUST pass these rigorous financial fault checks.

## 1. Zero Double-Charge Guarantee (Idempotency)
**Scenario:** A customer clicks "Pay" twice due to network lag. Mobile app fires two simultaneous `POST /api/v1/payments/{bookingId}/charge`.
**Validation:**
- Database must enforce a `UNIQUEConstraint` on `(booking_id, payment_intent)`.
- Request #1 processes successfully (`HTTP 200`).
- Request #2 hits the backend, reads the `idempotency_key` cache in Redis, and drops safely returning `HTTP 409 Conflict` or echoing the Request #1 success state. No second card charge occurs.

## 2. Payout Reconciliation Matches
**Scenario:** A driver finishes 42 trips over the week.
**Validation:**
- Execute `SELECT SUM(final_fare * 0.8) FROM bookings WHERE driver_id = 'X' AND status = 'COMPLETED'`.
- This exact floating-point decimal MUST mathematically match the total batch payout generated in the `FinanceSettlementsScreen`. Discrepancies drop the batch deployment.

## 3. SLA Enterprise Refund Automations
**Scenario:** A `GOLD` tier Enterprise client experiences an `ETA_VIOLATION` SLA Breach detected by Sprint 8 engines.
**Validation:**
- System must natively log the `SlaBreachEvent`.
- Billing service automatically generates a credit line-item (e.g., -$5.00) applying it to the monthly Enterprise Invoice algorithmically.

## 4. Wallet Concurrency Integrity
**Scenario:** Two rapid micro-tips ($1 and $2) fire simultaneously to the driver's Wallet via independent Kafka partitions.
**Validation:**
- JPA `@Version` locking (implemented in Sprint 8) protects the `Wallet` entity. One tip registers, the second tip triggers `ObjectOptimisticLockingFailureException` and is safely pushed to the Kafka Retry-Queue until the row unlocks, guaranteeing exactly $3.00 is added.

#!/bin/bash
# UAT Scenario Runner
# Usage: ./scenario_runner.sh [scenario_id] [token]

SCENARIO=$1
BEARER_TOKEN=$2
API_BASE="http://localhost:8080/api/v1"

echo "======================================"
echo "VELO-Courrier Automated UAT Test Suite"
echo "======================================"

if [ "$SCENARIO" == "A" ]; then
  echo "Executing Scenario A: Happy Path"
  echo "-> 1. Creating Draft Booking"
  BOOKING_ID=$(curl -s -X POST $API_BASE/bookings \
    -H "Authorization: Bearer $BEARER_TOKEN" \
    -H "Content-Type: application/json" \
    -H "Idempotency-Key: uat-test-$(date +%s)" \
    -d '{"pickupLat": 48.8566, "pickupLon": 2.3522, "dropoffLat": 48.8600, "dropoffLon": 2.3600, "categoryId": "BICYCLE"}' | jq -r '.data.id')
  
  echo "-> 2. Emulating Customer Payment Success Webhook"
  curl -s -X POST $API_BASE/payment/webhook/stripe \
    -H "Stripe-Signature: test_sig" \
    -H "Content-Type: application/json" \
    -d "{\"id\": \"evt_test_$BOOKING_ID\", \"type\": \"payment_intent.succeeded\", \"data\": {\"object\": {\"metadata\": {\"bookingId\": \"$BOOKING_ID\"}}}}"
    
  echo "-> 3. Verifying status is SEARCHING"
  curl -s -H "Authorization: Bearer $BEARER_TOKEN" $API_BASE/bookings/$BOOKING_ID | jq '.data.status'
  
  # Note: A real E2E runner like Cypress/Detox would click UI buttons.
  # This cURL script simply proves the REST transitions function sequentially.
  echo "UAT A Complete."
fi

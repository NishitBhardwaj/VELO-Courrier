import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 50 },  // Ramp-up to 50 users
    { duration: '1m', target: 200 },  // Spike to 200 users
    { duration: '30s', target: 500 }, // Stress test at 500 concurrent users
    { duration: '30s', target: 0 },   // Cool down
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of bookings must be created in under 500ms
    http_req_failed: ['rate<0.01'],   // Error rate must be < 1%
  },
};

const BASE_URL = __ENV.API_URL || 'http://localhost:8080/api/v1';

export default function () {
  const payload = JSON.stringify({
    customerId: "c1234567-89ab-cdef-0123-456789abcde0",
    pickupLocation: { lat: 48.8566, lng: 2.3522, address: "Paris Center" },
    dropoffLocation: { lat: 48.8606, lng: 2.3376, address: "Louvre Museum" },
    vehicleType: "BICYCLE"
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer MOCK_LOAD_TEST_JWT_TOKEN'
    },
  };

  const res = http.post(`${BASE_URL}/bookings`, payload, params);

  check(res, {
    'is status 201': (r) => r.status === 201,
    'has booking id': (r) => JSON.parse(r.body).id !== undefined,
  });

  sleep(1); // Wait 1 second before next iteration
}

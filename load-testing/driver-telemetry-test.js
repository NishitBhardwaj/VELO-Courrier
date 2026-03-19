import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  scenarios: {
    telemetry_flood: {
      executor: 'constant-vus',
      vus: 1000, // 1000 drivers online simultaneously
      duration: '2m',
    },
  },
  thresholds: {
    http_req_duration: ['p(95)<100'], // 95% of telemetry updates must process in under 100ms
    http_req_failed: ['rate<0.001'],  // Less than 0.1% failure rate for GPS drops
  },
};

const BASE_URL = __ENV.API_URL || 'http://localhost:8080/api/v1';

export default function () {
  // Simulate driver movement by slightly altering coordinates
  const driverLat = 48.8566 + (Math.random() * 0.01);
  const driverLng = 2.3522 + (Math.random() * 0.01);

  const payload = JSON.stringify({
    driverId: `d-mock-${__VU}`, // Unique Driver ID per Virtual User
    latitude: driverLat,
    longitude: driverLng,
    status: "AVAILABLE"
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer MOCK_DRIVER_TOKEN_${__VU}`
    },
  };

  const res = http.post(`${BASE_URL}/drivers/location`, payload, params);

  check(res, {
    'is status 200': (r) => r.status === 200,
  });

  sleep(5); // Simulate native app polling rate (e.g. 5 seconds per GPS ping)
}

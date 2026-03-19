import { api } from './axios';
import 'react-native-get-random-values'; // Required for uuid polyfill in React Native
import { v4 as uuidv4 } from 'uuid';

export interface BookingRequest {
  pickupLocation: { lat: number; lon: number; address: string };
  dropoffLocation: { lat: number; lon: number; address: string };
  vehicleCategoryId: string;
}

/**
 * Creates a booking by enforcing an Idempotency-Key.
 * This guarantees that if the user has a poor 3G connection and the 
 * response drops, the Axios retry logic will NOT create two identical bookings.
 */
export const createBooking = async (payload: BookingRequest) => {
  const idempotencyKey = uuidv4();
  
  const { data } = await api.post('/bookings', payload, {
    headers: {
      'Idempotency-Key': idempotencyKey
    }
  });

  return data;
};

export const fetchCurrentBooking = async () => {
  const { data } = await api.get('/bookings/current');
  return data;
};

export const calculateFare = async (pickupLat: number, pickupLon: number, dropoffLat: number, dropoffLon: number, categoryId: string) => {
  const { data } = await api.post('/pricing/estimate', {
    pickupLat, pickupLon, dropoffLat, dropoffLon, categoryId
  });
  return data;
};

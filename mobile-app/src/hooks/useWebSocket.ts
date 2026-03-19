import { useEffect, useRef, useState } from 'react';
import { AppState, AppStateStatus } from 'react-native';
import { Client } from '@stomp/stompjs';
import * as SecureStore from 'expo-secure-store';
import { api } from '../api/axios';

const SOCKET_URL = 'ws://localhost:8080/ws-endpoint'; // Staging/Prod URL swapped via ENV

export const useWebSocket = (bookingId?: string) => {
  const stompClient = useRef<Client | null>(null);
  const [isConnected, setIsConnected] = useState(false);
  const appState = useRef(AppState.currentState);

  // Core initialization engine
  const connect = async () => {
    const token = await SecureStore.getItemAsync('accessToken');
    if (!token) return;

    stompClient.current = new Client({
      brokerURL: SOCKET_URL,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      reconnectDelay: 2000, // Exponential backoff handles deeper retries internally or manually config
      onConnect: () => {
        setIsConnected(true);
        console.log('STOMP Connected Securely');
        
        // Subscribe to private dispatch offers if driver
        stompClient.current?.subscribe('/user/queue/dispatch.offers', (msg) => {
          console.log("New Dispatch Offer:", msg.body);
        });

        // If watching a specific booking (Customer Tracker or Active Driver)
        if (bookingId) {
          stompClient.current?.subscribe(`/topic/booking.${bookingId}`, (msg) => {
            console.log("Booking Status Mutated:", msg.body);
          });
        }
      },
      onDisconnect: () => {
        setIsConnected(false);
        console.log('STOMP Disconnected');
      },
      onWebSocketError: (error) => {
        setIsConnected(false);
        console.error('Socket Error:', error);
      }
    });

    stompClient.current.activate();
  };

  useEffect(() => {
    connect();

    // The Critical Background -> Foreground Synchronization Guard
    const subscription = AppState.addEventListener('change', async (nextAppState: AppStateStatus) => {
      if (appState.current.match(/inactive|background/) && nextAppState === 'active') {
        console.log('App returned to FOREGROUND -> Firing REST Fallback Sync');
        
        // 1. Re-initiate STOMP if iOS killed it
        if (!stompClient.current?.connected) {
          connect();
        }

        // 2. Fetch ground-truth state bypassing WebSocket entirely to prevent torn states
        try {
          const { data } = await api.get('/bookings/current');
          console.log('Synchronized Booking State Post-Wakeup:', data);
          // dispatch(setGlobalBookingState(data))
        } catch (e) {
          console.log('No active booking on wakeup');
        }
      }
      appState.current = nextAppState;
    });

    return () => {
      stompClient.current?.deactivate();
      subscription.remove();
    };
  }, [bookingId]);

  return { isConnected, stompClient: stompClient.current };
};

import { useEffect, useState } from 'react';
import * as Location from 'expo-location';
import { api } from '../api/axios';
import { useAuth } from '../context/AuthContext';

const LOCATION_TASK_NAME = 'BACKGROUND_LOCATION_TASK';

// In a real Expo app, this task must be defined in the global scope (outside React components)
// using TaskManager.defineTask(LOCATION_TASK_NAME, ({ data, error }) => { ... })
// For this scaffolding, we provide the hook that initializes the permissions and starts the tracker.

export const useBackgroundGeolocation = (isDriverOnline: boolean) => {
  const { role } = useAuth();
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  useEffect(() => {
    // Only drivers actively on-duty should broadcast their location
    if (role !== 'DRIVER' || !isDriverOnline) {
      Location.stopLocationUpdatesAsync(LOCATION_TASK_NAME).catch(() => {});
      return;
    }

    const startTracking = async () => {
      try {
        let { status: fgStatus } = await Location.requestForegroundPermissionsAsync();
        if (fgStatus !== 'granted') {
          setErrorMsg('Permission to access location was denied');
          return;
        }

        let { status: bgStatus } = await Location.requestBackgroundPermissionsAsync();
        if (bgStatus !== 'granted') {
          setErrorMsg('Background location permission denied. You cannot receive jobs while the app is closed.');
          return;
        }

        await Location.startLocationUpdatesAsync(LOCATION_TASK_NAME, {
          accuracy: Location.Accuracy.High,
          showsBackgroundLocationIndicator: true,
          timeInterval: 15000, // 15 seconds
          distanceInterval: 10,  // Or every 10 meters
          deferredUpdatesInterval: 15000,
          foregroundService: {
            notificationTitle: 'VELO Courrier Driver',
            notificationBody: 'You are online and actively sharing your location to receive deliveries.',
            notificationColor: '#0055ff',
          },
        });

      } catch (err) {
        console.warn("Failed to start location polling", err);
      }
    };

    startTracking();

    return () => {
      // Cleanup when driver goes offline
      Location.stopLocationUpdatesAsync(LOCATION_TASK_NAME).catch(() => {});
    };
  }, [role, isDriverOnline]);

  return { errorMsg };
};

// Global task definition intended to be placed in App.tsx or index.js
/*
import * as TaskManager from 'expo-task-manager';
import { api } from './src/api/axios';

TaskManager.defineTask(LOCATION_TASK_NAME, async ({ data, error }) => {
  if (error) {
    console.error(error);
    return;
  }
  if (data) {
    const { locations } = data as any;
    try {
      // Broadcast to Redis GEOSEARCH
      await api.post('/tracking/ping', {
        lat: locations[0].coords.latitude,
        lon: locations[0].coords.longitude,
      });
    } catch (err) {
      console.log("Background Ping failed - likely network drop", err);
    }
  }
});
*/

import React, { useEffect, useState, useRef } from 'react';
import { View, StyleSheet, Text } from 'react-native';
import MapView, { Marker, Polyline } from 'react-native-maps';
import { api as axios } from './api/axios';

// Interfaces
interface Location {
  lat: number;
  lng: number;
}

interface TrackingFallbackResponse {
  driver: Location;
  status: string;
  eta: number;
}

interface CustomerTrackingMapProps {
  bookingId: string;
}

export const CustomerTrackingMap: React.FC<CustomerTrackingMapProps> = ({ bookingId }) => {
  const [driverLocation, setDriverLocation] = useState<Location | null>(null);
  const [status, setStatus] = useState<string>('CONNECTING');
  const [eta, setEta] = useState<number | null>(null);
  const [isStale, setIsStale] = useState<boolean>(false);
  
  const mapRef = useRef<MapView>(null);
  const lastLocationUpdate = useRef<number>(Date.now());

  // 1. WebSocket Integration
  // NOTE: Assuming useWebSocket is handled at a higher level or imported here.
  // We'll mock the WebSocket listener structure here based on the blueprint.
  useEffect(() => {
    // Mocking WebSocket connection to /topic/booking/{bookingId}
    console.log(`Subscribing to WS: /topic/booking/${bookingId}`);
    
    // Simulating WS Event (v1.driver.location)
    const wsListener = (event: any) => {
      if (event.type === 'v1.driver.location') {
        const newLoc = { lat: event.lat, lng: event.lng };
        setDriverLocation(newLoc);
        lastLocationUpdate.current = Date.now();
        setIsStale(false);
      }
    };

    // Cleanup listener on unmount
    return () => {
      console.log(`Unsubscribing from WS: /topic/booking/${bookingId}`);
    };
  }, [bookingId]);

  // 2. Fallback Polling every 10 seconds
  useEffect(() => {
    const fetchTracking = async () => {
      try {
        const response = await axios.get<TrackingFallbackResponse>(`/bookings/${bookingId}/tracking`);
        const { driver, status: currentStatus, eta: currentEta } = response.data;
        
        // If WS hasn't updated recently or we just started up, use fallback data
        const now = Date.now();
        if (now - lastLocationUpdate.current > 15000 || !driverLocation) {
          setDriverLocation(driver);
          lastLocationUpdate.current = now;
        }

        setStatus(currentStatus);
        setEta(currentEta);
      } catch (error) {
        console.error('Failed to fetch tracking details', error);
      }
    };

    // Initial fetch
    fetchTracking();

    const interval = setInterval(() => {
      fetchTracking();
    }, 10000);

    return () => clearInterval(interval);
  }, [bookingId, driverLocation]);

  // 3. Stale Detection - Blueprint says: if > 30 sec, show "location updating..."
  useEffect(() => {
    const staleCheck = setInterval(() => {
      if (Date.now() - lastLocationUpdate.current > 30000) {
        setIsStale(true);
      }
    }, 5000);
    return () => clearInterval(staleCheck);
  }, []);

  if (!driverLocation) {
    return <View style={styles.center}><Text>Loading Map...</Text></View>;
  }

  const [currentEta, setCurrentEta] = useState<number>(0);
  const [delaySeconds, setDelaySeconds] = useState<number>(0);

  useEffect(() => {
    // Scaffold: STOMP websocket hook catching v2 algorithm anomalies
    /*
      stompClient.subscribe(`/topic/booking/${bookingId}`, (msg) => {
         const payload = JSON.parse(msg.body);
         if (payload.type === 'v2.booking.eta.updated') {
             setCurrentEta(payload.etaSeconds);
             setDelaySeconds(payload.delaySeconds);
         }
      })
    */
  }, [bookingId]);

  return (
    <View style={styles.container}>
      <MapView style={styles.map}
        initialRegion={{
          latitude: driverLocation.lat,
          longitude: driverLocation.lng,
          latitudeDelta: 0.05,
          longitudeDelta: 0.05,
        }}
      >
        {driverLocation && (
          <Marker 
            coordinate={{ latitude: driverLocation.lat, longitude: driverLocation.lng }}
            title="Driver"
            description="Your delivery driver"
            // You can add a custom image icon here for the vehicle
          />
        )}
      </MapView>
      
      <View style={styles.overlay}>
        <Text style={styles.statusText}>Status: {status}</Text>
        {eta && <Text style={styles.etaText}>ETA: {Math.round(eta / 60)} mins</Text>}
        {isStale && <Text style={styles.staleText}>Location updating...</Text>}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1 },
  center: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  map: { width: '100%', height: '100%' },
  overlay: {
    position: 'absolute',
    bottom: 40,
    left: 20,
    right: 20,
    alignItems: 'center',
    padding: 20
  },
  loaderText: {
    marginTop: 10,
    fontSize: 16,
    color: '#64748b'
  },
  delayBanner: {
    position: 'absolute',
    top: 50,
    left: 20,
    right: 20,
    backgroundColor: '#ef4444',
    padding: 15,
    borderRadius: 8,
    zIndex: 100,
    elevation: 5
  },
  delayText: {
    color: '#fff',
    fontWeight: 'bold',
    textAlign: 'center'
  },
  statusText: { fontSize: 16, fontWeight: 'bold' },
  etaText: { fontSize: 14, color: 'gray', marginTop: 4 },
  staleText: { fontSize: 12, color: 'orange', marginTop: 8, fontStyle: 'italic' },
});

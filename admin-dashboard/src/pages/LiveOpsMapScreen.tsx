import React, { useEffect, useState } from 'react';
import { SideDetailDrawer } from '../components/SideDetailDrawer';

// Mocked interfaces translating from backend DTOs
export interface OpsSnapshot {
  drivers: DriverPoint[];
  activeBookings: any[];
  timestamp: number;
}

export interface DriverPoint {
  driverId: string;
  lat: number;
  lng: number;
  status: string; // AVAILABLE, ASSIGNED, IN_TRANSIT, STALE, OFFLINE
}

export const LiveOpsMapScreen: React.FC = () => {
  const [snapshot, setSnapshot] = useState<OpsSnapshot | null>(null);
  const [selectedDriver, setSelectedDriver] = useState<DriverPoint | null>(null);
  const [anomalies, setAnomalies] = useState<string[]>([]); // SPRINT 5

  useEffect(() => {
    // 1. Initial REST fetch
    const fetchMap = async () => {
      try {
        const response = await fetch('/api/v1/admin/ops/map');
        const data = await response.json();
        setSnapshot(data);
      } catch (err) {
        console.error("Failed to load map data", err);
      }
    };
    fetchMap();

    // 2. STOMP Subscription (Pseudo-code for Dashboard websocket hooks)
    /*
      stompClient.subscribe("/topic/admin/ops/live", (msg) => {
        const event = JSON.parse(msg.body);
        if (event.type === 'v2.booking.deviation.alert') {
           setAnomalies(prev => [`CRITICAL: Driver off route by ${event.distanceMeters}m!`, ...prev]);
        }
      });
    */

    // 3. Fallback Interval Loop (30s)
    const interval = setInterval(fetchMap, 30000);
    return () => clearInterval(interval);
  }, []);

  const getMarkerColor = (status: string) => {
    switch (status) {
      case 'STALE': return '#eab308'; // Yellow
      case 'OFFLINE': return '#94a3b8'; // Gray
      case 'IN_TRANSIT': return '#3b82f6'; // Blue
      default: return '#22c55e'; // Green (Available)
    }
  };

  return (
    <div style={styles.container}>
      {/* MAP LAYER: Placeholder for react-map-gl or google-maps-react */}
      <div style={styles.mapArea}>
         {/* SPRINT 5 DEVIATION ALERTS PANEL */}
         {anomalies.length > 0 && (
           <div style={styles.alertPanel}>
             <h3 style={{ margin: 0, color: '#ef4444' }}>⚠️ Live Anomalies</h3>
             {anomalies.map((a, i) => <div key={i} style={{ marginTop: 5, fontSize: 12 }}>{a}</div>)}
           </div>
         )}
         
         {!snapshot ? <div>Loading Global Radar...</div> : (
            <div>
              <p style={{ position: 'absolute', top: 10, left: 10, zIndex: 10, background: 'white', padding: 5 }}>
                 Active Drivers: {snapshot.drivers.length} | Last Sync: {new Date(snapshot.timestamp).toLocaleTimeString()}
              </p>
              
              {/* Markers Overlay */}
              {snapshot.drivers.map(driver => (
                <div 
                   key={driver.driverId}
                   onClick={() => setSelectedDriver(driver)}
                   style={{
                     ...styles.marker, 
                     backgroundColor: getMarkerColor(driver.status),
                     // Math translation for lat/lng to screen pixels would happen here in a real map wrapper
                     position: 'relative', margin: 10
                   }}
                >
                  🚚 
                </div>
              ))}
            </div>
         )}
      </div>

      {/* ADMIN DETAIL DRAWER */}
      {selectedDriver && (
         <SideDetailDrawer driver={selectedDriver} onClose={() => setSelectedDriver(null)} />
      )}
    </div>
  );
};

const styles = {
  container: { display: 'flex', width: '100vw', height: '100vh', overflow: 'hidden' },
  mapArea: { flex: 1, backgroundColor: '#f1f5f9', position: 'relative' as const, display: 'flex', alignItems: 'center', justifyContent: 'center' },
  marker: { width: 30, height: 30, borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', cursor: 'pointer', border: '2px solid white', boxShadow: '0 2px 5px rgba(0,0,0,0.2)' },
  alertPanel: { position: 'absolute' as const, top: 10, right: 10, width: 300, backgroundColor: 'white', border: '2px solid #ef4444', borderRadius: 8, padding: 10, zIndex: 50, boxShadow: '0 4px 12px rgba(0,0,0,0.1)' }
};

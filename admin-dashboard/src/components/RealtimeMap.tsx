import React, { useEffect, useState } from 'react';
import { Client } from '@stomp/stompjs';

interface LocationUpdate {
  driverId: string;
  lat: number;
  lon: number;
  accuracy: number;
}

export const RealtimeMap: React.FC<{ token: string }> = ({ token }) => {
  const [activeDrivers, setActiveDrivers] = useState<Record<string, LocationUpdate>>({});

  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws-endpoint',
      connectHeaders: { Authorization: `Bearer ${token}` },
      onConnect: () => {
        // Admin consumes the firehose topic of all active driver locations
        client.subscribe('/topic/tracking.firehose', (msg) => {
          const update: LocationUpdate = JSON.parse(msg.body);
          
          setActiveDrivers(prev => ({
            ...prev,
            [update.driverId]: update
          }));
        });
      }
    });

    client.activate();
    return () => { client.deactivate(); };
  }, [token]);

  return (
    <div className="bg-slate-800 p-8 rounded-lg shadow-xl text-white">
      <h3 className="text-xl font-bold mb-4">Live Dispatch Radar</h3>
      <div className="h-96 w-full bg-slate-700 rounded border border-slate-600 relative overflow-hidden flex items-center justify-center">
        {/* In production, this div is replaced by <GoogleMap> or <MapContainer> (Leaflet) */}
        <p className="text-slate-400 absolute opacity-50 text-center">
          Map rendering engine omitted.<br/>
          Tracking {Object.keys(activeDrivers).length} live active drivers.
        </p>
        
        {Object.values(activeDrivers).map(driver => (
          <div 
            key={driver.driverId}
            className="absolute w-4 h-4 bg-green-500 rounded-full shadow-[0_0_15px_rgba(34,197,94,0.8)] animate-pulse"
            style={{ 
              // Hypothetical coordinate mapping to screen pixel space
              left: `${(driver.lon + 180) * (100 / 360)}%`, 
              top: `${(90 - driver.lat) * (100 / 180)}%` 
            }}
          />
        ))}
      </div>
    </div>
  );
};

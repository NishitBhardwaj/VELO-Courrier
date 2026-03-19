import React, { useState, useEffect } from 'react';

export const OperationsAnalyticsScreen: React.FC = () => {
  const [kpis, setKpis] = useState<any>(null);

  useEffect(() => {
    // Scaffold Fetch: GET /api/v1/admin/analytics/kpis
    setTimeout(() => {
      setKpis({
        activeBookings: 42,
        dailyVolume: 840,
        completionRate: "94%",
        cancellationRate: "3%",
        averageDelayMinutes: 4.2,
        deviationIncidents: 12,
        multiStopAdoption: "28%"
      });
    }, 500);
  }, []);

  if (!kpis) return <div style={styles.container}><h2>Loading Global Enterprise Metrics...</h2></div>;

  return (
    <div style={styles.container}>
      <h2>Business Analytics & Operations</h2>
      <p style={{ color: 'gray' }}>Live SLA and system throughput tracking.</p>

      <div style={styles.grid}>
         <div style={styles.card}>
            <p style={styles.label}>Active System Bookings</p>
            <h1 style={{ ...styles.value, color: '#3b82f6' }}>{kpis.activeBookings}</h1>
         </div>
         <div style={styles.card}>
            <p style={styles.label}>Daily Volume</p>
            <h1 style={styles.value}>{kpis.dailyVolume}</h1>
         </div>
         <div style={styles.card}>
            <p style={styles.label}>Completion Rate</p>
            <h1 style={{ ...styles.value, color: '#22c55e' }}>{kpis.completionRate}</h1>
         </div>
         <div style={styles.card}>
            <p style={styles.label}>Cancellation Rate</p>
            <h1 style={{ ...styles.value, color: '#ef4444' }}>{kpis.cancellationRate}</h1>
         </div>
         <div style={styles.card}>
            <p style={styles.label}>Avg Route Delay (Mins)</p>
            <h1 style={{ ...styles.value, color: '#eab308' }}>{kpis.averageDelayMinutes}</h1>
         </div>
         <div style={styles.card}>
            <p style={styles.label}>Route Anomalies Caught</p>
            <h1 style={{ ...styles.value, color: '#ef4444' }}>{kpis.deviationIncidents}</h1>
         </div>
      </div>

      <div style={styles.heatMapPlaceholder}>
         <h3>Zone Demand Heatmap</h3>
         {/* Placeholder for future Mapbox GL JS layer or Chart.js */}
         <div style={{ flex: 1, backgroundColor: '#f1f5f9', display: 'flex', alignItems: 'center', justifyContent: 'center', borderRadius: 8 }}>
            <p style={{ color: 'gray' }}>Geospatial Surcharge & Demand Plugin Hooks Here</p>
         </div>
      </div>

    </div>
  );
};

const styles = {
  container: { padding: '20px', fontFamily: 'sans-serif', height: '100vh', display: 'flex', flexDirection: 'column' as const },
  grid: { display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: '20px', marginTop: '20px' },
  card: { padding: '20px', borderRadius: '8px', border: '1px solid #e2e8f0', backgroundColor: 'white', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' },
  label: { margin: 0, fontSize: '14px', color: '#64748b', fontWeight: 'bold' },
  value: { margin: '10px 0 0 0', fontSize: '32px' },
  heatMapPlaceholder: { marginTop: 30, display: 'flex', flexDirection: 'column' as const, flex: 1 }
};

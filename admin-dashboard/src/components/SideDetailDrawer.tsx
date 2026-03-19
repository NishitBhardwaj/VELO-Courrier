import React from 'react';

export const SideDetailDrawer: React.FC<{ driver: any, onClose: () => void }> = ({ driver, onClose }) => {
  return (
    <div style={styles.drawer}>
      <button onClick={onClose} style={styles.closeBtn}>✕</button>
      
      <h2 style={styles.title}>Driver Telemetry</h2>
      
      <div style={styles.card}>
        <p><strong>ID:</strong> {driver.driverId.substring(0, 8)}...</p>
        <p><strong>Status:</strong> <span style={{ color: driver.status === 'STALE' ? 'orange' : 'black', fontWeight: 'bold' }}>{driver.status}</span></p>
        <p><strong>Coordinates:</strong> {parseFloat(driver.lat).toFixed(4)}, {parseFloat(driver.lng).toFixed(4)}</p>
      </div>

      <div style={styles.actions}>
        <button style={{ ...styles.actionBtn, backgroundColor: '#0055ff', color: 'white' }}>View Active Booking Route</button>
        <button style={{ ...styles.actionBtn, backgroundColor: '#ef4444', color: 'white' }}>Force Offline</button>
        <button style={{ ...styles.actionBtn, backgroundColor: '#f1f5f9', color: 'black' }}>Call Driver</button>
      </div>
    </div>
  );
};

const styles = {
  drawer: { 
    width: '350px', 
    backgroundColor: '#fff', 
    boxShadow: '-4px 0 15px rgba(0,0,0,0.1)', 
    padding: '20px', 
    display: 'flex', 
    flexDirection: 'column' as const,
    zIndex: 100
  },
  title: { fontSize: 20, marginBottom: 20, color: '#1e293b' },
  closeBtn: { alignSelf: 'flex-end', background: 'none', border: 'none', fontSize: 20, cursor: 'pointer', color: '#64748b' },
  card: { backgroundColor: '#f8fafc', padding: 15, borderRadius: 8, marginBottom: 20 },
  actions: { display: 'flex', flexDirection: 'column' as const, gap: '10px' },
  actionBtn: { padding: '12px', border: 'none', borderRadius: 6, fontWeight: 'bold', cursor: 'pointer' }
};

import React, { useState, useEffect } from 'react';

export const AiDecisionLogsScreen: React.FC = () => {
  const [logs, setLogs] = useState<any[]>([]);

  useEffect(() => {
    // Scaffold GET /api/v1/admin/ai/logs
    setTimeout(() => {
      setLogs([
         { bookingId: 'b-999', driverAssigned: 'd-111', aiScore: 0.98, model: 'v1-heuristic', type: 'DISPATCH_RANKING', time: '18:05' },
         { bookingId: '-', driverAssigned: '-', aiScore: null, model: 'demand-rfr-v2', type: 'DEMAND_PREDICT: 45 hits in Z1', time: '18:00' }
      ]);
    }, 500);
  }, []);

  return (
    <div style={styles.container}>
      <h2>AI Optimization Logs & Diagnostics</h2>
      <p style={{ color: 'gray' }}>System governance observing raw mathematical models and dispatch rankings natively.</p>

      <div style={styles.alertBox}>
         ✅ <strong>System Status Header:</strong> AI Fast-API is ONLINE. Fallback rule engine strictly monitoring sub-400ms pings.
      </div>

      <table style={styles.table}>
         <thead>
           <tr>
             <th style={styles.th}>Timestamp</th>
             <th style={styles.th}>Diagnostic Type</th>
             <th style={styles.th}>Model Version</th>
             <th style={styles.th}>Booking ID</th>
             <th style={styles.th}>Score/Probability</th>
           </tr>
         </thead>
         <tbody>
           {logs.map((L, i) => (
             <tr key={i} style={styles.row}>
                <td>{L.time}</td>
                <td>{L.type}</td>
                <td>
                   <span style={styles.badge}>{L.model}</span>
                </td>
                <td>{L.bookingId}</td>
                <td style={{ fontWeight: 'bold' }}>{L.aiScore ? `${(L.aiScore * 100).toFixed(0)}% Match` : '-'}</td>
             </tr>
           ))}
         </tbody>
      </table>
    </div>
  );
};

const styles = {
  container: { padding: 20, fontFamily: 'sans-serif' },
  alertBox: { padding: 15, backgroundColor: '#dcfce7', color: '#166534', borderRadius: 8, marginBottom: 20, border: '1px solid #bbf7d0' },
  table: { width: '100%', borderCollapse: 'collapse' as const },
  th: { textAlign: 'left' as const, padding: 10, borderBottom: '2px solid #e2e8f0', color: '#475569' },
  row: { borderBottom: '1px solid #e2e8f0', height: 45 },
  badge: { fontSize: 11, padding: '4px 8px', backgroundColor: '#f1f5f9', borderRadius: 4, fontWeight: 'bold' }
};

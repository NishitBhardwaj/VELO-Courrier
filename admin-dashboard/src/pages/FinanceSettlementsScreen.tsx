import React, { useState, useEffect } from 'react';

export const FinanceSettlementsScreen: React.FC = () => {
  const [settlements, setSettlements] = useState<any[]>([]);

  useEffect(() => {
    // Scaffold: GET /api/v1/admin/finance/settlements
    setTimeout(() => {
       setSettlements([
          { driverId: 'd-101', name: 'John Doe', trips: 42, payout: 1050.50, status: 'SCHEDULED', date: '2026-06-15' },
          { driverId: 'd-789', name: 'Alain D.', trips: 15, payout: 345.20, status: 'PAID', date: '2026-06-01' }
       ]);
    }, 500);
  }, []);

  return (
    <div style={styles.container}>
      <h2>Finance & Settlements</h2>
      <p style={{ color: 'gray' }}>Enterprise B2B billing flows and Driver Payout Reconciliations.</p>

      <div style={styles.tabBar}>
         <button style={{ ...styles.tab, borderBottom: '3px solid #3b82f6', color: '#3b82f6' }}>Driver Payouts</button>
         <button style={styles.tab}>Enterprise Invoicing</button>
         <button style={styles.tab}>SLA Adjustments</button>
      </div>

      <table style={styles.table}>
         <thead>
           <tr>
             <th style={styles.th}>Driver ID</th>
             <th style={styles.th}>Name</th>
             <th style={styles.th}>Billing Cycle Trips</th>
             <th style={styles.th}>Total Payout ($)</th>
             <th style={styles.th}>Status</th>
             <th style={styles.th}>Execution Date</th>
             <th style={styles.th}>Actions</th>
           </tr>
         </thead>
         <tbody>
           {settlements.map((s, i) => (
             <tr key={i} style={styles.row}>
                <td>{s.driverId}</td>
                <td>{s.name}</td>
                <td>{s.trips}</td>
                <td style={{ fontWeight: 'bold' }}>${s.payout.toFixed(2)}</td>
                <td>
                   <span style={{ 
                      padding: 5, borderRadius: 4, fontSize: 12, 
                      backgroundColor: s.status === 'PAID' ? '#dcfce7' : '#fef08a',
                      color: s.status === 'PAID' ? '#166534' : '#854d0e'
                   }}>
                     {s.status}
                   </span>
                </td>
                <td>{s.date}</td>
                <td>
                   {s.status === 'SCHEDULED' && <button style={styles.actionBtn}>Process Batch</button>}
                   {s.status === 'PAID' && <button style={{ ...styles.actionBtn, backgroundColor: '#f1f5f9', color: 'black' }}>View Receipt</button>}
                </td>
             </tr>
           ))}
         </tbody>
      </table>
    </div>
  );
};

const styles = {
  container: { padding: 20, fontFamily: 'sans-serif' },
  tabBar: { display: 'flex', borderBottom: '1px solid #e2e8f0', marginBottom: 20 },
  tab: { padding: '10px 20px', background: 'none', border: 'none', cursor: 'pointer', fontSize: 16, color: '#64748b' },
  table: { width: '100%', borderCollapse: 'collapse' as const },
  th: { textAlign: 'left' as const, padding: 10, borderBottom: '2px solid #e2e8f0', color: '#475569' },
  row: { borderBottom: '1px solid #e2e8f0', height: 50 },
  actionBtn: { padding: '8px 12px', backgroundColor: '#3b82f6', color: 'white', border: 'none', borderRadius: 6, cursor: 'pointer' }
};

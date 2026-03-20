import React from 'react';
import { View, StyleSheet, Text } from 'react-native';
import { BookingResponse } from '../types';

interface RouteSummaryCardProps {
  booking: BookingResponse;
}

export const RouteSummaryCard: React.FC<RouteSummaryCardProps> = ({ booking }) => {
  return (
    <View style={styles.card}>
      <Text style={styles.title}>Route Details</Text>
      
      <View style={styles.row}>
        <Text style={styles.label}>Total Fare:</Text>
        <Text style={styles.value}>${booking.fareEstimate?.toFixed(2) || '0.00'}</Text>
      </View>

      <View style={styles.row}>
        <Text style={styles.label}>Total Stops:</Text>
        <Text style={styles.value}>{booking.totalStops}</Text>
      </View>

      <View style={styles.row}>
        <Text style={styles.label}>Distance:</Text>
        <Text style={styles.value}>
          {/* @ts-ignore */}
          {booking.routeDistanceMeters ? (booking.routeDistanceMeters / 1000).toFixed(1) + ' km' : 'Calculating...'}
        </Text>
      </View>
      
      {booking.stops && booking.stops.map((stop) => (
        <Text key={stop.order} style={styles.stopText}>
          {stop.order}. {stop.type.replace('_', ' ')}: {stop.contactName}
        </Text>
      ))}
    </View>
  );
};

const styles = StyleSheet.create({
  card: { backgroundColor: '#fff', padding: 15, borderRadius: 8, shadowOpacity: 0.1, shadowRadius: 4, elevation: 2, marginBottom: 15 },
  title: { fontSize: 18, fontWeight: 'bold', marginBottom: 10, color: '#1e293b' },
  row: { flexDirection: 'row', justifyContent: 'space-between', marginBottom: 5 },
  label: { fontSize: 14, color: '#64748b' },
  value: { fontSize: 14, fontWeight: 'bold', color: '#0f172a' },
  stopText: { marginTop: 10, fontSize: 13, color: '#475569', fontStyle: 'italic' }
});

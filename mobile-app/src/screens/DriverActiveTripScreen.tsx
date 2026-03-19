import React, { useState, useEffect } from 'react';
import { View, StyleSheet, Text, Button, Alert, ActivityIndicator } from 'react-native';
import { useActiveBooking } from '../context/ActiveBookingContext';
import { api as axios } from '../api/axios';

export const DriverActiveTripScreen: React.FC = () => {
  const { activeBooking, fetchBooking, updateStopProgress, isLoading } = useActiveBooking();
  const [updating, setUpdating] = useState(false);

  // Fallback payload fetching mechanism
  useEffect(() => {
    // Hardcoded bookingId for scaffolding
    const scaffoldBookingId = "active-booking-uuid-12345";
    if (!activeBooking) {
      fetchBooking(scaffoldBookingId);
    }
    
    // In a real app, STOMP Websocket v2 listener hooks here:
    /*
      stompClient.subscribe(`/topic/booking/${scaffoldBookingId}`, (message) => {
        const data = JSON.parse(message.body);
        if (data.type === 'v2.booking.stop.updated') {
            updateStopProgress(data.stopOrder, data.status, data.nextStopOrder);
        }
      });
    */
  }, []);

  if (isLoading || !activeBooking) return <ActivityIndicator size="large" style={styles.center} />;

  // Multi-Stop Logic
  const stops = activeBooking.stops || [];
  const currentStop = stops.find(s => s.order === activeBooking.currentStopOrder);
  const nextStop = stops.find(s => s.order === activeBooking.currentStopOrder + 1);

  const handleUpdateStatus = async (newStatus: string) => {
    if (!currentStop) return;
    setUpdating(true);
    try {
      if (newStatus === 'COMPLETED') {
        // Enforce POD check or Route to PODCaptureScreen
        // navigation.navigate('PODCapture', { stopId: currentStop.id })
      }

      await axios.patch(`/bookings/${activeBooking.id}/stops/${currentStop.id}/status`, {
        status: newStatus
      });

      // Optimistic Local Update (WebSocket will usually handle this seamlessly)
      updateStopProgress(currentStop.order, newStatus, newStatus === 'COMPLETED' ? currentStop.order + 1 : currentStop.order);
      
      if (newStatus === 'COMPLETED' && !nextStop) {
         Alert.alert('Trip Finished', 'You have completed all stops in this manifest!');
      }

    } catch (err: any) {
      Alert.alert('Error', err.response?.data?.message || 'Failed to update stop status');
    } finally {
      setUpdating(false);
    }
  };

  if (!currentStop) {
    return (
      <View style={styles.center}>
        <Text style={styles.finishedText}>All Stops Completed 🎉</Text>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <Text style={styles.header}>Active Trip Flow</Text>
      
      <View style={styles.activeStopCard}>
        <Text style={styles.stopPhaseText}>CURRENT STOP (Order: {currentStop.order}/{activeBooking.totalStops})</Text>
        <Text style={styles.stopType}>{currentStop.type.replace('_', ' ')}</Text>
        <Text style={styles.contactInfo}>Name: {currentStop.contactName}</Text>
        <Text style={styles.contactInfo}>Phone: {currentStop.contactPhone}</Text>

        <View style={styles.actions}>
          {currentStop.status === 'PENDING' ? (
             <Button title="Mark Arrived" onPress={() => handleUpdateStatus('ARRIVED')} disabled={updating} />
          ) : (
            <>
             <View style={{ marginBottom: 10 }}>
               <Button title="Complete Stop" color="green" onPress={() => handleUpdateStatus('COMPLETED')} disabled={updating} />
             </View>
             <Button title="Report Issue / Fail" color="red" onPress={() => handleUpdateStatus('FAILED')} disabled={updating} />
            </>
          )}
        </View>
      </View>

      {nextStop && (
        <View style={styles.nextStopPreview}>
          <Text style={styles.previewTitle}>Next Stop Preview</Text>
          <Text style={styles.contactInfo}>{nextStop.type} - {nextStop.contactName}</Text>
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  center: { flex: 1, justifyContent: 'center', alignItems: 'center' },
  container: { flex: 1, backgroundColor: '#f8fafc', padding: 15 },
  header: { fontSize: 22, fontWeight: 'bold', marginBottom: 20 },
  activeStopCard: { backgroundColor: '#fff', padding: 20, borderRadius: 12, borderWidth: 2, borderColor: '#0055ff', elevation: 4 },
  stopPhaseText: { color: '#0055ff', fontWeight: 'bold', fontSize: 12, marginBottom: 8 },
  stopType: { fontSize: 24, fontWeight: 'bold', color: '#1e293b', marginBottom: 10 },
  contactInfo: { fontSize: 16, color: '#475569', marginBottom: 5 },
  actions: { marginTop: 25 },
  nextStopPreview: { backgroundColor: '#e2e8f0', padding: 15, borderRadius: 8, marginTop: 20 },
  previewTitle: { fontSize: 14, fontWeight: 'bold', color: '#64748b', marginBottom: 5 },
  finishedText: { fontSize: 24, fontWeight: 'bold', color: 'green' }
});

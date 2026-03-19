import React, { useState } from 'react';
import { View, StyleSheet, Text, TextInput, Button, ScrollView, Alert, TouchableOpacity } from 'react-native';
import { StopDto, MultiStopBookingRequest, BookingResponse } from '../types';
import { api as axios } from '../api/axios';

export const MultiStopBookingScreen: React.FC = () => {
  const [stops, setStops] = useState<StopDto[]>([
    { order: 1, type: 'PICKUP', contactName: '', contactPhone: '', addressJson: '{}' }
  ]);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleAddStop = () => {
    if (stops.length >= 5) {
      Alert.alert('Limit Reached', 'Maximum of 5 stops allowed.');
      return;
    }
    const newOrder = stops.length + 1;
    setStops([...stops, { order: newOrder, type: 'DROP_OFF', contactName: '', contactPhone: '', addressJson: '{}' }]);
  };

  const handleRemoveStop = (index: number) => {
    if (index === 0) {
      Alert.alert('Invalid', 'You cannot remove the Pickup stop.');
      return;
    }
    const updatedStops = stops.filter((_, i) => i !== index).map((stop, i) => ({
      ...stop,
      order: i + 1
    }));
    setStops(updatedStops);
  };

  const updateStopField = (index: number, field: string, value: string) => {
    const updatedStops = [...stops];
    (updatedStops[index] as any)[field] = value;
    setStops(updatedStops);
  };

  const handleSubmit = async () => {
    setIsSubmitting(true);
    try {
      const payload: MultiStopBookingRequest = {
        serviceType: 'COURIER',
        stops
      };
      
      const response = await axios.post<BookingResponse>('/bookings/multi-stop', payload);
      Alert.alert('Success', `Booking created: ${response.data.id}`);
      // Usually would navigation.navigate('BookingDetails', { id: response.data.id })
    } catch (error: any) {
      Alert.alert('Error', error.response?.data?.message || 'Failed to create booking');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <ScrollView style={styles.container}>
      <Text style={styles.title}>Plan Multi-Stop Delivery</Text>
      
      {stops.map((stop, index) => (
        <View key={index} style={styles.stopCard}>
          <Text style={styles.stopHeader}>
            {stop.type === 'PICKUP' ? '🛫 Pickup' : `📍 Drop-off ${index}`} (Stop {stop.order})
          </Text>
          
          <TextInput 
            style={styles.input} 
            placeholder="Contact Name" 
            value={stop.contactName}
            onChangeText={(t) => updateStopField(index, 'contactName', t)}
          />
          <TextInput 
            style={styles.input} 
            placeholder="Contact Phone" 
            value={stop.contactPhone}
            onChangeText={(t) => updateStopField(index, 'contactPhone', t)}
            keyboardType="phone-pad"
          />

          {index > 0 && (
            <TouchableOpacity onPress={() => handleRemoveStop(index)} style={styles.removeBtn}>
              <Text style={styles.removeText}>Remove Stop</Text>
            </TouchableOpacity>
          )}
        </View>
      ))}

      <View style={styles.actionRow}>
        <Button title="+ Add Drop-off" onPress={handleAddStop} disabled={stops.length >= 5} />
      </View>

      <View style={styles.submitRow}>
        <Button 
          title={isSubmitting ? "Creating..." : "Confirm & Book Delivery"} 
          onPress={handleSubmit} 
          disabled={isSubmitting} 
          color="#0055ff" 
        />
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: { flex: 1, padding: 16, backgroundColor: '#f1f5f9' },
  title: { fontSize: 24, fontWeight: 'bold', marginBottom: 20 },
  stopCard: { backgroundColor: '#fff', padding: 16, borderRadius: 8, marginBottom: 12, shadowColor: '#000', shadowOpacity: 0.05, shadowRadius: 5, elevation: 2 },
  stopHeader: { fontSize: 16, fontWeight: 'bold', marginBottom: 12, color: '#334155' },
  input: { borderWidth: 1, borderColor: '#cbd5e1', padding: 10, borderRadius: 6, marginBottom: 10 },
  removeBtn: { alignSelf: 'flex-end', marginTop: 5 },
  removeText: { color: 'red', fontSize: 14, fontWeight: '500' },
  actionRow: { marginVertical: 15 },
  submitRow: { marginBottom: 40 },
});

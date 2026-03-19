import { StatusBar } from 'expo-status-bar';
import { StyleSheet, SafeAreaView } from 'react-native';
import { CustomerTrackingMap } from './src/CustomerTrackingMap';

export default function App() {
  return (
    <SafeAreaView style={styles.container}>
      <CustomerTrackingMap bookingId="test-booking-123" />
      <StatusBar style="auto" />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
});

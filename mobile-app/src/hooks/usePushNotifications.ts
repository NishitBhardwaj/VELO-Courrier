import { useState, useEffect, useRef } from 'react';
import { Platform } from 'react-native';
import * as Device from 'expo-device';
import * as Notifications from 'expo-notifications';
import Constants from 'expo-constants';
import { api } from '../api/axios';
import { useAuth } from '../context/AuthContext';

Notifications.setNotificationHandler({
  handleNotification: async () => ({
    shouldShowAlert: true,
    shouldPlaySound: true,
    shouldSetBadge: false,
    shouldShowBanner: true,
    shouldShowList: true
  }),
});

export const usePushNotifications = () => {
  const [expoPushToken, setExpoPushToken] = useState<string | undefined>();
  const [notification, setNotification] = useState<any>(false);
  const notificationListener = useRef<any>(null);
  const responseListener = useRef<any>(null);
  const { accessToken } = useAuth(); // Dependency to re-trigger registering when logged in

  useEffect(() => {
    if (!accessToken) return;

    registerForPushNotificationsAsync().then(token => {
      setExpoPushToken(token);
      // Immediately hydrate the Backend with this token
      if (token) {
        api.post('/auth/device-token', { token, OS: Platform.OS }).catch(() => console.log('Silent token update fail'));
      }
    });

    // Foreground listener
    notificationListener.current = Notifications.addNotificationReceivedListener((notification: any) => {
      setNotification(notification);
      // Example: If driver gets "Offer" push notification while in App, trigger local beep.
    });

    // Tap/Background listener
    responseListener.current = Notifications.addNotificationResponseReceivedListener((response: any) => {
      console.log('User tapped Notification', response);
      // Router naturally pushes to /booking/{id} 
    });

    return () => {
      if (notificationListener.current) {
        notificationListener.current.remove();
      }
      if (responseListener.current) {
        responseListener.current.remove();
      }
    };
  }, [accessToken]);

  return { expoPushToken, notification };
};

async function registerForPushNotificationsAsync() {
  let token;
  if (Platform.OS === 'android') {
    await Notifications.setNotificationChannelAsync('default', {
      name: 'default',
      importance: Notifications.AndroidImportance.MAX,
      vibrationPattern: [0, 250, 250, 250],
      lightColor: '#FF231F7C',
    });
  }

  if (Device.isDevice) {
    const { status: existingStatus } = await Notifications.getPermissionsAsync();
    let finalStatus = existingStatus;
    if (existingStatus !== 'granted') {
      const { status } = await Notifications.requestPermissionsAsync();
      finalStatus = status;
    }
    if (finalStatus !== 'granted') {
      console.log('Failed to get push token for push notification!');
      return;
    }
    token = (await Notifications.getExpoPushTokenAsync({
      projectId: Constants.expoConfig?.extra?.eas?.projectId,
    })).data;
  } else {
    console.log('Must use physical device for Push Notifications');
  }

  return token;
}

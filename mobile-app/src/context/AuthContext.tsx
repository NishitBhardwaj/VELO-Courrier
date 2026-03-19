import React, { createContext, useContext, useState, useEffect } from 'react';
import * as SecureStore from 'expo-secure-store';
import { api } from '../api/axios';

interface AuthState {
  accessToken: string | null;
  role: string | null;
  userId: string | null;
  isLoading: boolean;
}

interface AuthContextType extends AuthState {
  login: (phoneOrEmail: string, otp: string) => Promise<void>;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [authState, setAuthState] = useState<AuthState>({
    accessToken: null,
    role: null,
    userId: null,
    isLoading: true,
  });

  useEffect(() => {
    // Bootstrap securely stored tokens
    const loadTokens = async () => {
      try {
        const token = await SecureStore.getItemAsync('accessToken');
        const role = await SecureStore.getItemAsync('role');
        const userId = await SecureStore.getItemAsync('userId');
        
        if (token) {
          setAuthState({ accessToken: token, role, userId, isLoading: false });
        } else {
          setAuthState(prev => ({ ...prev, isLoading: false }));
        }
      } catch (error) {
        setAuthState(prev => ({ ...prev, isLoading: false }));
      }
    };
    loadTokens();
  }, []);

  const login = async (username: string, password: string) => {
    const { data } = await api.post('/auth/login', { username, password });
    
    await SecureStore.setItemAsync('accessToken', data.accessToken);
    await SecureStore.setItemAsync('refreshToken', data.refreshToken);
    await SecureStore.setItemAsync('role', data.role);
    await SecureStore.setItemAsync('userId', data.userId);

    setAuthState({
      accessToken: data.accessToken,
      role: data.role,
      userId: data.userId,
      isLoading: false,
    });
  };

  const logout = async () => {
    await SecureStore.deleteItemAsync('accessToken');
    await SecureStore.deleteItemAsync('refreshToken');
    await SecureStore.deleteItemAsync('role');
    await SecureStore.deleteItemAsync('userId');
    setAuthState({ accessToken: null, role: null, userId: null, isLoading: false });
  };

  return (
    <AuthContext.Provider value={{ ...authState, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

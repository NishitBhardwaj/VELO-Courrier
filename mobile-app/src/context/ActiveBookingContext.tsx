import React, { createContext, useContext, useState, useEffect } from 'react';
import { BookingResponse } from '../types';
import { api as axios } from '../api/axios';
import { useAuth } from './AuthContext';

interface ActiveBookingContextType {
  activeBooking: BookingResponse | null;
  fetchBooking: (id: string) => Promise<void>;
  updateStopProgress: (stopOrder: number, status: string, nextStopOrder: number) => void;
  clearBooking: () => void;
  isLoading: boolean;
}

const ActiveBookingContext = createContext<ActiveBookingContextType>(null as any);

export const ActiveBookingProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { role } = useAuth();
  const [activeBooking, setActiveBooking] = useState<BookingResponse | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const fetchBooking = async (id: string) => {
    setIsLoading(true);
    try {
      const response = await axios.get<BookingResponse>(`/bookings/${id}`);
      setActiveBooking(response.data);
    } catch (err) {
      console.error('Failed to fetch active booking details', err);
    } finally {
      setIsLoading(false);
    }
  };

  const updateStopProgress = (stopOrder: number, status: string, nextStopOrder: number) => {
    setActiveBooking((prev) => {
      if (!prev) return prev;
      return {
        ...prev,
        currentStopOrder: nextStopOrder,
        stops: prev.stops?.map(s => s.order === stopOrder ? { ...s, status } : s)
      };
    });
  };

  const clearBooking = () => setActiveBooking(null);

  return (
    <ActiveBookingContext.Provider value={{ activeBooking, fetchBooking, updateStopProgress, clearBooking, isLoading }}>
      {children}
    </ActiveBookingContext.Provider>
  );
};

export const useActiveBooking = () => useContext(ActiveBookingContext);

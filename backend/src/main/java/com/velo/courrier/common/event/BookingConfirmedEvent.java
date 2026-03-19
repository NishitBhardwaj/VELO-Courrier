package com.velo.courrier.common.event;

import lombok.Value;

import java.util.UUID;

@Value
public class BookingConfirmedEvent {
    UUID bookingId;
}

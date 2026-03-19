package com.velo.courrier.booking.chat;

import com.velo.courrier.booking.Booking;
import com.velo.courrier.booking.BookingRepository;
import com.velo.courrier.common.enums.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final BookingRepository bookingRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public List<ChatMessage> getBookingHistory(UUID bookingId, String requestorId) {
        // Simple verification that booking exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        return chatMessageRepository.findByBookingIdOrderByCreatedAtAsc(bookingId);
    }

    @Transactional
    public ChatMessage sendMessage(UUID bookingId, String senderId, String messageBody) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chat is disabled for completed or canceled bookings");
        }

        // Determine receiver
        String customerId = booking.getCustomer() != null ? booking.getCustomer().getId().toString() : "";
        String driverId = booking.getDriver() != null ? booking.getDriver().getId().toString() : "";
        
        String receiverId = senderId.equals(customerId) ? driverId : customerId;
        if (receiverId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Counterparty not yet assigned to this booking");
        }

        ChatMessage message = ChatMessage.builder()
                .bookingId(bookingId)
                .senderId(UUID.fromString(senderId))
                .receiverId(UUID.fromString(receiverId))
                .message(messageBody)
                .isRead(false)
                .build();

        message = chatMessageRepository.save(message);

        // Broadcast over WebSockets
        log.info("Broadcasting chat message {} to /topic/booking/{}/chat", message.getId(), bookingId);
        messagingTemplate.convertAndSend("/topic/booking/" + bookingId + "/chat", message);

        return message;
    }
}

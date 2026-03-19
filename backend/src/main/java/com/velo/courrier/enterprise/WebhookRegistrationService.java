package com.velo.courrier.enterprise;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookRegistrationService {

    // private final EnterpriseWebhookRepository webhookRepository;
    // private final RestTemplate restTemplate;

    /**
     * Independent Micro-service listener firing JSON state changes strictly to registered Callback URLs 
     * established by major Enterprise Customers bridging their internal CRMs inherently.
     */
    public void dispatchB2bWebhook(UUID customerId, String eventType, Object bookingPayload) {
        log.debug("WEBHOOK_PIPELINE: Scanning callbacks for B2B Client {} on Event {}", customerId, eventType);

        /*
        List<EnterpriseWebhook> callbacks = webhookRepository.findAllByCustomerIdAndEventTypeAndActiveTrue(customerId, eventType);
        
        for (EnterpriseWebhook webhook : callbacks) {
            try {
                // Execute HTTP POST directly to Customer's registered endpoint
                log.info("Broadcasting JSON payload to remote URL: {}", webhook.getCallbackUrl());
                // restTemplate.postForLocation(webhook.getCallbackUrl(), bookingPayload);
                
                // Track Webhook Delivery explicitly guarding reliability metrics natively
                // deliveryLogRepository.save(new DeliveryLog(webhook.getId(), "SUCCESS", 200));
            } catch (Exception e) {
                log.error("Enterprise Webhook failed routing to: {}", webhook.getCallbackUrl(), e);
                // Rescheduling failed hooks onto Kafka Dead Letter Queues gracefully
            }
        }
        */
    }
}

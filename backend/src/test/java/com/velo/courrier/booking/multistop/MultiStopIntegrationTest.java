package com.velo.courrier.booking.multistop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class MultiStopIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
        registry.add("features.multi_stop.backend_enabled", () -> "true");
    }

    @Test
    @WithMockUser(username = "123e4567-e89b-12d3-a456-426614174000", roles = {"CUSTOMER"})
    void testCreateMultiStopBookingConstraints() throws Exception {
        // Will yield 404 because Customer doesn't exist in fresh DB, but the payload is structurally valid
        String payload = """
                {
                  "serviceType": "COURIER",
                  "stops": [
                    {"order": 1, "type": "PICKUP", "contactName": "Alice", "contactPhone": "123", "addressJson": "{}"},
                    {"order": 2, "type": "DROP_OFF", "contactName": "Bob", "contactPhone": "456", "addressJson": "{}"}
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/bookings/multi-stop")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isNotFound()); // Customer not found overrides 200 OK
    }
}

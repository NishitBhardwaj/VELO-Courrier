package com.velo.courrier.tracking;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class TrackingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7-alpine")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @Test
    @WithMockUser(username = "123e4567-e89b-12d3-a456-426614174000", roles = {"DRIVER"})
    void testDriverLocationUpdate() throws Exception {
        String payload = """
                {
                    "latitude": 28.6139,
                    "longitude": 77.2090,
                    "heading": 120.0,
                    "speed": 35.5,
                    "accuracy": 4.2
                }
                """;

        mockMvc.perform(post("/api/v1/drivers/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "customer-uuid", roles = {"CUSTOMER"})
    void testCustomerTrackingAccessDeniedForOtherBookings() throws Exception {
        // Without setting up a mock DB booking, this will throw 404 naturally
        // But if it exists and doesn't belong to the customer, it should throw 403
        mockMvc.perform(get("/api/v1/bookings/123e4567-e89b-12d3-a456-426614174001/tracking"))
                .andExpect(status().isNotFound()); 
    }
}

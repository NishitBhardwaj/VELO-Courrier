package com.velo.courrier.booking.pod;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class PodIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageService storageService; // Mock MinIO for unit testing the controller flow

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @WithMockUser(username = "123e4567-e89b-12d3-a456-426614174000", roles = {"DRIVER"})
    void testUploadProofOfDeliveryNotFoundBooking() throws Exception {
        when(storageService.uploadFileAndGetSignedUrl(any(), any())).thenReturn("http://mock-url");

        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "mock-image-data".getBytes());
        MockMultipartFile signature = new MockMultipartFile("signature", "sig.png", MediaType.IMAGE_PNG_VALUE, "mock-sig".getBytes());

        UUID bookingId = UUID.randomUUID();

        // Will yield 404 because the booking doesn't exist in the fresh test DB
        mockMvc.perform(multipart("/api/v1/bookings/" + bookingId + "/pod")
                .file(photo)
                .file(signature)
                .param("receiverName", "John Doe")
                .param("otp", "1234"))
                .andExpect(status().isNotFound());
    }
}

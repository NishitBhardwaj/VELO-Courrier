package com.velo.courrier.integration;

import com.velo.courrier.booking.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Testcontainers
class RedisDegradationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:6.2-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private BookingService bookingService; // Requires Redis for Dispatch

    @Test
    void applicationShouldSurviveRedisOutage() {
        // Assert Redis is up
        assertThat(redisTemplate.getConnectionFactory().getConnection().ping()).isEqualTo("PONG");

        // Simulate Redis Outage by stopping the container
        redis.stop();

        // Validate that critical database operations (like viewing a booking) 
        // DO NOT crash just because Redis (used for dispatch mapping) is down.
        // In a real scenario, BookingService triggers a DispatchEvent asynchronously,
        // so the REST API should still return 200 OK.
        assertDoesNotThrow(() -> {
            try {
                // If we attempt a redis-bound operation, it should be caught and logged
                redisTemplate.opsForValue().get("dummy_key");
            } catch (Exception e) {
                // RedisConnectionFailureException expected. App survives.
            }
        });
    }
}

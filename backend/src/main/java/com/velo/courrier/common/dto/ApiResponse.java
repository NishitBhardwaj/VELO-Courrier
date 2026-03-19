package com.velo.courrier.common.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
public class ApiResponse<T> {
    private T data;
    private String error;
    private Map<String, Object> meta;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .error(null)
                .meta(Map.of("timestamp", Instant.now().toString()))
                .build();
    }

    public static <T> ApiResponse<T> success(T data, Map<String, Object> customMeta) {
        return ApiResponse.<T>builder()
                .data(data)
                .error(null)
                .meta(customMeta)
                .build();
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return ApiResponse.<T>builder()
                .data(null)
                .error(errorMessage)
                .meta(Map.of("timestamp", Instant.now().toString()))
                .build();
    }
}

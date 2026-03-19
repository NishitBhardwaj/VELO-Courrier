package com.velo.courrier.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private String id;
    private String phone;
    private String email;
    private String role;
    private boolean isActive;
}

package com.service_booking_system.service.dto;

import com.service_booking_system.service.enums.UserRoles;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String jwtToken;
    private String username;
    private String role;
}

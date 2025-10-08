package com.service_booking_system.service.dto.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerificationRequest {
    private String username;
    private String otp;
}


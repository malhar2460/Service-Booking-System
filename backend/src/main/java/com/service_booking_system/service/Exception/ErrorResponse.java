package com.service_booking_system.service.Exception;

import lombok.*;

// Store error related data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
}

package com.service_booking_system.service.dto.Customer;

import lombok.Data;

@Data
public class UserUpdateDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String email;
}

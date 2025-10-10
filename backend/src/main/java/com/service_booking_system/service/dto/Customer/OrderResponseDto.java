package com.service_booking_system.service.dto.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String contactName;
    private String contactPhone;
    private String contactAddress;
    private LocalDate date;
    private LocalTime time;
    private String status;

    private String serviceProviderName;  // optional extra field
}

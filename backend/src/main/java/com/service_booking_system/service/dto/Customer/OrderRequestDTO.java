package com.service_booking_system.service.dto.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Long userId;
    private Long serviceProviderId;
    private Long subServiceId;
    private String contactName;
    private String contactPhone;
    private String contactAddress;
    private String date;
    private String time;
}



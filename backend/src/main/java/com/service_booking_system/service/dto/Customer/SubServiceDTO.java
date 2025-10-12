package com.service_booking_system.service.dto.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubServiceDTO {
    private Long subServiceId;
    private String subServiceName;
    private double price;
}

package com.service_booking_system.service.dto.Service_Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PendingOrderDTO {
    private Long orderId;
    private LocalDate date;
    private LocalTime time;
    private String contactName;
    private String contactPhone;
    private String contactAddress;
    private String serviceName;
    private String subServiceName;
    private Double charge;
}


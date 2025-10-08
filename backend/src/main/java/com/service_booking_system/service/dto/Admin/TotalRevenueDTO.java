package com.service_booking_system.service.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TotalRevenueDTO {
    private Long orderId;
    private LocalDateTime date;
    private String customerName;
    private Double totalPaid;
    private Double providerPayout;
    private Double adminRevenue;
}
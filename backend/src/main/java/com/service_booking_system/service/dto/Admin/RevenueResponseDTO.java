package com.service_booking_system.service.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueResponseDTO {
    private Double totalRevenue;
    private Long totalOrders;
    private Double grossSales;
    private Double serviceProviderPayouts;
    private Double avgOrderValue;
}
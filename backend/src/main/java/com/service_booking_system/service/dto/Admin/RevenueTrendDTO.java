package com.service_booking_system.service.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueTrendDTO {
    private String title; // e.g., "Monthly Revenue"
    private List<RevenueGraphPointDTO> data;
}

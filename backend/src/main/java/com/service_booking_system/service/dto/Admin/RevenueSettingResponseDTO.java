// /backend/src/main/java/com/service_booking_system/service/dto/Admin/RevenueSettingResponseDTO.java

package com.service_booking_system.service.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueSettingResponseDTO {
    private Double serviceProviderRevenue;
    private String message;
}

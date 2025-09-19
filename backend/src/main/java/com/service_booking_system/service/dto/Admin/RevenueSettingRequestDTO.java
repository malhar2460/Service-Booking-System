package com.service_booking_system.service.dto.Admin;

import com.service_booking_system.service.enums.CurrentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueSettingRequestDTO {

    @NotNull(message = "Service provider revenue part is required.")
    private Double serviceProviderRevenue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CurrentStatus currentStatus;

}

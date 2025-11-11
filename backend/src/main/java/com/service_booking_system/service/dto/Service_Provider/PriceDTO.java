package com.service_booking_system.service.dto.Service_Provider;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDTO {

    @NotNull(message = "Price is required.")
    private Long price;

    private String subServiceName;

}

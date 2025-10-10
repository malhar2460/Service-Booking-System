package com.service_booking_system.service.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateDTO {

    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "State name contains invalid characters.")
    private String stateName;
}

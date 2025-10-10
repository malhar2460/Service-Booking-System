package com.service_booking_system.service.dto;

import com.service_booking_system.service.model.Cities;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {

    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "City name contains invalid characters.")
    private String cityName;

    private String stateName;

    public CityDTO(Cities cities) {
        this.cityName = cities.getCityName();
        this.stateName = cities.getStates().getStateName();
    }
}


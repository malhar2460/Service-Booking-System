package com.service_booking_system.service.dto;

import com.service_booking_system.service.model.Cities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityNameDTO {
    private String cityName;

    public CityNameDTO(Cities cities) {
        this.cityName = cities.getCityName();
    }
}

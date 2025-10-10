package com.service_booking_system.service.dto;

import com.service_booking_system.service.model.States;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StateNameDTO {
    private String stateName;

    public StateNameDTO(States states) {
        this.stateName = states.getStateName();
    }
}

package com.service_booking_system.service.controller;

import com.service_booking_system.service.dto.StateNameDTO;
import com.service_booking_system.service.repository.StatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/states")
@RequiredArgsConstructor
public class StateController {

    @Autowired private StatesRepository statesRepository;

    // http://localhost:8080/states
    // Return all state names
    @GetMapping(produces = "application/json")
    public List<StateNameDTO> getAllStates() {
        return statesRepository.findAll()
                .stream()
                .map(StateNameDTO::new)
                .toList();
    }

}

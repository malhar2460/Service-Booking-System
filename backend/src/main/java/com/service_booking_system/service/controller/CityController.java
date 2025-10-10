package com.service_booking_system.service.controller;

import com.service_booking_system.service.dto.CityDTO;
import com.service_booking_system.service.dto.CityNameDTO;
import com.service_booking_system.service.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
@RequiredArgsConstructor
public class CityController {

    @Autowired  private CityRepository cityRepository;

    // http://localhost:8080/cities
    // Return all the cities with state name
    @GetMapping(produces = "application/json")
    public List<CityDTO> getAllCities() {
        return cityRepository.findAll()
                .stream()
                .map(CityDTO::new)
                .toList();
    }

    // http://localhost:8080/cities/get/{stateName}
    // Return city based on state name
    @GetMapping("/get/{stateName}")
    public List<CityNameDTO> getCitiesByState(@PathVariable String stateName) {
        return cityRepository.findByStates_StateName(stateName)
                .stream()
                .map(CityNameDTO::new)
                .toList();
    }

}


package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.Cities;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<Cities, Long> {

    Optional<Cities> findByCityName(String cityName);

}

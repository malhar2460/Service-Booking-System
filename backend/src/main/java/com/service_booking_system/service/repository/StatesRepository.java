package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.States;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatesRepository extends JpaRepository<States, Long> {
    States findByStateName(String stateName);
}

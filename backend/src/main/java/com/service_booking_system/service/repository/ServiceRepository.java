package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Services, Long> {

    @Query("SELECT s.serviceName FROM Services s")
    List<String> findAllServiceName();

    Optional<Services> findByServiceName(String serviceName);
}

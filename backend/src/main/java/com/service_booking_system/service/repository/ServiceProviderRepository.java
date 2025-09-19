package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.ServiceProvider;
import com.service_booking_system.service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    Optional<ServiceProvider> getByUser(Users user);

    ServiceProvider findByUser(Users user);
}

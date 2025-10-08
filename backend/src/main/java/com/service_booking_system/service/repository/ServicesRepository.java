package com.service_booking_system.service.repository;

import com.service_booking_system.service.dto.Customer.ServiceDTO;
import com.service_booking_system.service.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ServicesRepository extends JpaRepository<Services, Long> {

    @Query("SELECT new com.service_booking_system.service.dto.Customer.ServiceDTO(s.serviceId, s.serviceName, s.serviceImage) FROM Services s")
    List<ServiceDTO> findAllServices();
}

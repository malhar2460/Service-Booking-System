// /backend/src/main/java/com/service_booking_system/service/repository/SubServiceRepository.java

package com.service_booking_system.service.repository;

import com.service_booking_system.service.dto.Customer.SubServiceDTO;
import com.service_booking_system.service.model.Services;
import com.service_booking_system.service.model.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubServiceRepository extends JpaRepository<SubServices, Long> {

    @Query("SELECT new com.service_booking_system.service.dto.Customer.SubServiceDTO(" +
            "s.subServiceId, s.subServiceName, p.amount) " +
            "FROM SubServices s " +
            "JOIN Prices p ON s.subServiceId = p.subServices.subServiceId " +
            "WHERE s.services.serviceId = :serviceId")
    List<SubServiceDTO> findSubServicesWithPriceByServiceId(Long serviceId);

    long countByServices(Services service);

    List<SubServices> findByServices(Services service);

    SubServices findBySubServiceName(String subServiceName);
}
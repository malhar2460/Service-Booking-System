// /backend/src/main/java/com/service_booking_system/service/repository/SubServiceRepository.java

package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.Services;
import com.service_booking_system.service.model.SubServices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubServiceRepository extends JpaRepository<SubServices, Long> {
    long countByServices(Services service);

    List<SubServices> findByServices(Services service);
}
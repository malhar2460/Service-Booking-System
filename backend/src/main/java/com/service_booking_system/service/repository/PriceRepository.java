// /backend/src/main/java/com/service_booking_system/service/repository/PriceRepository.java

package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.Prices;
import com.service_booking_system.service.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Prices, Long> {

    List<Prices> findByServiceProvider(ServiceProvider serviceProvider);
    Optional<Prices> findBySubServices_SubServiceId(String subServiceId);
}

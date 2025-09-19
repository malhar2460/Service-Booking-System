package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.Prices;
import com.service_booking_system.service.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Prices, Long> {

    List<Prices> findByServiceProvider(ServiceProvider serviceProvider);

}

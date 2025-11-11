package com.service_booking_system.service.repository;

import com.service_booking_system.service.enums.OrderStatus;
import com.service_booking_system.service.model.Orders;
import com.service_booking_system.service.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUsers_UserId(Long userId);

    List<Orders> findByServiceProviderAndStatus(ServiceProvider serviceProvider, OrderStatus orderStatus);
}

package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}

package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.Bill;
import com.service_booking_system.service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByBill(Bill bill);
}

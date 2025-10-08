package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.AdminRevenue;
import com.service_booking_system.service.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AdminRevenueRepository extends JpaRepository<AdminRevenue, Long> {

    @Query("""
    SELECT SUM(ar.profit)
    FROM AdminRevenue ar
    WHERE ar.createdAt BETWEEN :start AND :end""")
    Double getAdminRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    AdminRevenue findByPayment(Payment payment);
}


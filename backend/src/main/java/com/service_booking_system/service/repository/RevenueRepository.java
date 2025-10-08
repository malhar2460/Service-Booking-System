package com.service_booking_system.service.repository;

import com.service_booking_system.service.dto.Admin.TotalRevenueDTO;
import com.service_booking_system.service.model.AdminRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RevenueRepository extends JpaRepository<AdminRevenue, Long> {

    @Query("SELECT COALESCE(SUM(r.profit), 0.0) FROM AdminRevenue r WHERE r.createdAt BETWEEN :start AND :end")
    Double findTotalRevenueByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(COUNT(o), 0) FROM Orders o WHERE o.createdAt BETWEEN :start AND :end")
    Long findTotalOrdersByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(b.total), 0.0) FROM Bill b WHERE b.order.createdAt BETWEEN :start AND :end")
    Double findGrossSalesByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(p.finalAmount), 0.0) FROM Payout p WHERE p.createdAt BETWEEN :start AND :end")
    Double findPayoutsByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(AVG(b.total), 0.0) FROM Bill b WHERE b.order.createdAt BETWEEN :start AND :end")
    Double findAverageOrderValueByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
        SELECT new com.service_booking_system.service.dto.Admin.TotalRevenueDTO(
            o.orderId,
            ar.createdAt,
            CONCAT(c.firstName, ' ', c.lastName),
            b.total,
            p.finalAmount,
            ar.profit
        )
        FROM AdminRevenue ar
        JOIN ar.payment pay
        JOIN pay.bill b
        JOIN b.order o
        JOIN o.users c
        JOIN Payout p ON p.payment = pay 
        WHERE ar.createdAt BETWEEN :start AND :end
        ORDER BY ar.createdAt DESC
    """)
    List<TotalRevenueDTO> getAdminRevenueBreakdownBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}

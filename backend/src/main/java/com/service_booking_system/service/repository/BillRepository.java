package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.Bill;
import com.service_booking_system.service.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    @Query("""
    SELECT SUM(b.total)
    FROM Bill b
    WHERE b.order.createdAt BETWEEN :start AND :end""")
    Double getGrossSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Bill findByOrder(Orders order);
    Optional<Bill> findByOrder_OrderId(Long orderId);

}

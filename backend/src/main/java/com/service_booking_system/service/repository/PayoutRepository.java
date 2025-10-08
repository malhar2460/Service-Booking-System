package com.service_booking_system.service.repository;

import com.service_booking_system.service.dto.Admin.ServiceProviderRevenueTableDTO;
import com.service_booking_system.service.model.Payment;
import com.service_booking_system.service.model.Payout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PayoutRepository extends JpaRepository<Payout, Long> {

    @Query("SELECT SUM(p.finalAmount) FROM Payout p WHERE p.serviceProvider.serviceProviderId = :userId AND p.createdAt BETWEEN :start AND :end")
    Double getRevenueForUserInRange(@Param("userId") Long userId,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);

    @Query("""
        SELECT new com.service_booking_system.service.dto.Admin.ServiceProviderRevenueTableDTO(
            sp.serviceProviderId,
            SUM(p.finalAmount),
            SUM(p.charge),
            MAX(p.dateTime),
            COUNT(p)
        )
        FROM Payout p
        JOIN p.serviceProvider sp
        WHERE p.createdAt BETWEEN :start AND :end
        GROUP BY sp.serviceProviderId
        ORDER BY SUM(p.finalAmount) DESC
    """)
    List<ServiceProviderRevenueTableDTO> findAllProviderRevenuesInRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT SUM(p.finalAmount)
        FROM Payout p
        WHERE p.createdAt BETWEEN :start AND :end""")
    Double getProviderPayoutBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    Payout findByPayment(Payment payment);
}


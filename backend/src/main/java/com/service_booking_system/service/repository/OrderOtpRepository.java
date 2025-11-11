package com.service_booking_system.service.repository;

import com.service_booking_system.service.enums.OtpPurpose;
import com.service_booking_system.service.model.OrderOtp;
import com.service_booking_system.service.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOtpRepository extends JpaRepository<OrderOtp, Long> {
    OrderOtp findTopByOrderAndPurposeAndIsUsedFalseOrderByGeneratedAtDesc(Orders order, OtpPurpose purpose);
}

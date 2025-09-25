// /backend/src/main/java/com/service_booking_system/service/repository/RevenueBreakDownRepository.java

package com.service_booking_system.service.repository;

import com.service_booking_system.service.enums.CurrentStatus;
import com.service_booking_system.service.model.RevenueBreakDown;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RevenueBreakDownRepository extends JpaRepository<RevenueBreakDown, Long> {
    RevenueBreakDown findByCurrentStatus(CurrentStatus currentStatus);
}

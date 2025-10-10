package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}

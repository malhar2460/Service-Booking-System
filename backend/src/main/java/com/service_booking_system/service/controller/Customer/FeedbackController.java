package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.dto.Customer.FeedbackRequestDto;
import com.service_booking_system.service.service.Customer.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    @PostMapping("/submit/{userId}")
    public ResponseEntity<String> submitFeedback(
            @PathVariable Long userId,
            @RequestBody FeedbackRequestDto dto
    ) {
        try {
            feedbackService.submitFeedbackProviders(userId, dto);
            return ResponseEntity.ok("Feedback submitted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}


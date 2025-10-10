package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.controller.Admin.RepeatedCode;
import com.service_booking_system.service.dto.Customer.FeedbackRequestDto;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.Customer.FeedbackService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    @Autowired
    private RepeatedCode repeatedCode;
    @Autowired
    private UserRepository usersRepository;
    @PostMapping("/submit")
    public ResponseEntity<String> submitFeedback(
            HttpServletRequest request,
            @RequestBody FeedbackRequestDto dto
    ) {
        try {
            // 1️⃣ Extract userId from JWT token
            Long userId = repeatedCode.fetchUserIdFromToken(request);

            // 2️⃣ Optional: check if user is blocked
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found."));
            if (user.isBlocked()) {
                throw new AccessDeniedException("Your account is blocked. Cannot submit feedback.");
            }

            // 3️⃣ Submit feedback for the logged-in user
            feedbackService.submitFeedbackProviders(userId, dto);
            return ResponseEntity.ok("Feedback submitted successfully");
        } catch (RuntimeException | AccessDeniedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}


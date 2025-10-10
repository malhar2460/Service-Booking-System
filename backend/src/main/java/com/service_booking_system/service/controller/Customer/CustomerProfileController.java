package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.controller.Admin.RepeatedCode;
import com.service_booking_system.service.dto.Customer.UserUpdateDto;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.Customer.CustomerProfileService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/customer/profile")
@RequiredArgsConstructor
public class CustomerProfileController {

    @Autowired
    private final CustomerProfileService customerProfileService;
    @Autowired
    private final UserRepository usersRepository;
    @Autowired
    private final RepeatedCode repeatedCode;

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(
            HttpServletRequest request,
            @RequestBody UserUpdateDto dto
    ) throws AccessDeniedException {

        // 1️⃣ Extract userId from JWT token
        long userId = repeatedCode.fetchUserIdFromToken(request);

        // 2️⃣ Fetch the user by ID
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // 3️⃣ Check if the user is blocked
        if (user.isBlocked()) {
            throw new AccessDeniedException("Your account is blocked by admin. You cannot perform this action.");
        }

        // 4️⃣ Ensure DTO has correct userId
        dto.setUserId(userId);

        // 5️⃣ Update profile
        customerProfileService.updateUserProfile(dto);

        return ResponseEntity.ok("User profile updated successfully.");
    }


}


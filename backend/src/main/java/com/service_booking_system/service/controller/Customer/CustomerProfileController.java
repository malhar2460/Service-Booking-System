package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.dto.Customer.UserUpdateDto;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.Customer.CustomerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody UserUpdateDto dto) throws AccessDeniedException {
        Users user = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isBlocked()) {
            throw new AccessDeniedException("Your account is blocked by admin. You cannot perform this action.");
        }
        customerProfileService.updateUserProfile(dto);
        return ResponseEntity.ok("User profile updated successfully.");
    }
}


package com.service_booking_system.service.controller.Service_Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service_booking_system.service.dto.Service_Provider.ServiceProviderRequestDTO;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.Service_Provider.ProviderRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/sp")
public class ProviderRequestController {

    @Autowired private ObjectMapper objectMapper;

    @Autowired private UserRepository userRepository;

    @Autowired private ProviderRequestService providerRequestService;

    private void checkIfBlocked(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isBlocked()) {
            throw new RuntimeException("Your account is blocked by admin. You cannot perform this action.");
        }
    }

    // http://localhost:8080/sp/complete-sp-profile/{userId}
    // Render a form for service provider to submit their profile.
    @PostMapping(value ="/complete-sp-profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> completeServiceProviderProfile(
            @PathVariable Long userId,
            @RequestPart("data") String data,
            @RequestPart("aadharCard") MultipartFile aadharCard,
            @RequestPart(value = "panCard", required = false) MultipartFile panCard,
            @RequestPart("utilityBill") MultipartFile utilityBill,
            @RequestPart("profilePhoto") MultipartFile profilePhoto
    ) {
        try {
            checkIfBlocked(userId);
            ServiceProviderRequestDTO dto = objectMapper.readValue(data, ServiceProviderRequestDTO.class);
            String response = providerRequestService.completeServiceProviderProfile(userId, dto, aadharCard, panCard, utilityBill, profilePhoto);

            System.out.println("Raw JSON string from 'data': " + data);
            System.out.println("Aadhar file name: " + aadharCard.getOriginalFilename() + " | size: " + aadharCard.getSize());
            System.out.println("Utility Bill file name: " + utilityBill.getOriginalFilename() + " | size: " + utilityBill.getSize());
            System.out.println("Profile Photo file name: " + profilePhoto.getOriginalFilename() + " | size: " + profilePhoto.getSize());

            if (panCard != null) {
                System.out.println("PAN Card file name: " + panCard.getOriginalFilename() + " | size: " + panCard.getSize());
            } else {
                System.out.println("PAN Card not provided.");
            }

            return ResponseEntity.ok(Map.of("message", response));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid JSON format for profile data.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Unexpected error occurred.");
        }
    }
}

package com.service_booking_system.service.controller.Service_Provider;

import com.service_booking_system.service.controller.Admin.RepeatedCode;
import com.service_booking_system.service.dto.Admin.ServiceProviderResponseDTO;
import com.service_booking_system.service.dto.ChangePasswordRequestDTO;
import com.service_booking_system.service.service.ChangePasswordService;
import com.service_booking_system.service.service.Service_Provider.ProviderProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/provider")
public class ProviderProfileController {

    @Autowired private RepeatedCode repeatedCode;

    @Autowired private ChangePasswordService changePasswordService;

    @Autowired private ProviderProfileService providerProfileService;

    // http://localhost:8080/provider/profile
    // Fetch service provider profile
    @GetMapping("/profile")
    public ResponseEntity<ServiceProviderResponseDTO> getServiceProviderProfile(HttpServletRequest request) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(providerProfileService.getServiceProviderProfileDetail(userId));
    }

    // http://localhost:8080/sp-profile/change-password
    // Change password for service provider
    @PutMapping("/profile/change-password")
    public ResponseEntity<String> changeServiceProviderPassword(
            HttpServletRequest request,
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO
    ) throws Exception {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(changePasswordService.changePassword(userId, changePasswordRequestDTO));
    }

}


package com.service_booking_system.service.controller.Admin;

import com.service_booking_system.service.dto.Admin.ServiceProviderRequestDTO;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.service.Admin.RequestService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired private RequestService requestService;

    @Autowired private RepeatedCode repeatedCode;

    // http://localhost:8080/request/provider-requests
    // Return all the pending requests of service provider.
    @GetMapping("/provider-requests")
    public ResponseEntity<List<ServiceProviderRequestDTO>> getAllPendingProvidersProfiles(HttpServletRequest request) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        List<ServiceProviderRequestDTO> profiles = requestService.getAllProviderProfiles();
        return ResponseEntity.ok(profiles);
    }

    // http://localhost:8080/request/accept-provider/{userId}
    // Accpet service provider request.
    @PutMapping("/accept-provider/{userId}")
    public ResponseEntity<String> acceptProviderRequest(HttpServletRequest request, @PathVariable Long userId) throws AccessDeniedException {
        long adminId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(adminId);
        repeatedCode.isAdmin(user);
        try {
            String message = requestService.acceptProvider(userId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + e.getMessage());
        }
    }

    // http://localhost:8080/request/reject-provider/{userId}
    // Reject service provider request.
    @PutMapping("/reject-provider/{userId}")
    public ResponseEntity<String> rejectProviderRequest(HttpServletRequest request, @PathVariable Long userId) throws AccessDeniedException {
        long adminId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(adminId);
        repeatedCode.isAdmin(user);
        try {
            String message = requestService.rejectProvider(userId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + e.getMessage());
        }
    }

}
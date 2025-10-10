package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.Admin.CancelOrderService;
import com.service_booking_system.service.service.Customer.OrderService;
import com.service_booking_system.service.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cancel")
public class CancelOrderController {

    private final OrderService orderService;
    private final JWTService jwtService;
    private final UserRepository usersRepository;
    private final CancelOrderService cancelOrderService;
    private Long extractUserIdFromRequest(HttpServletRequest request) {
        Object userIdObj = jwtService.extractUserId(jwtService.extractTokenFromHeader(request));
        if (userIdObj == null) {
            throw new RuntimeException("User ID not found in token");
        }
        String userIdStr = userIdObj.toString();  // convert Object to String
        try {
            return Long.parseLong(userIdStr);     // parse String to Long
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid user ID in token");
        }
    }



    private void checkIfUserIsBlocked(Long userId) throws AccessDeniedException {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isBlocked()) {
            throw new AccessDeniedException("Your account is blocked by admin. You cannot perform this action.");
        }
    }
    @PostMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(HttpServletRequest request, @PathVariable Long orderId) throws AccessDeniedException {
        Long userId = extractUserIdFromRequest(request);
        checkIfUserIsBlocked(userId);
        cancelOrderService.cancelOrder(userId, orderId);
        return ResponseEntity.ok("Order canceled successfully");
    }
}

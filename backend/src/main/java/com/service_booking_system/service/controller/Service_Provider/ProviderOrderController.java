package com.service_booking_system.service.controller.Service_Provider;

import com.service_booking_system.service.controller.Admin.RepeatedCode;
import com.service_booking_system.service.dto.Service_Provider.PendingOrderDTO;
import com.service_booking_system.service.enums.OrderStatus;
import com.service_booking_system.service.enums.OtpPurpose;
import com.service_booking_system.service.model.Orders;
import com.service_booking_system.service.repository.OrderRepository;
import com.service_booking_system.service.service.Service_Provider.ProviderOrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/provider/orders")
@RequiredArgsConstructor
public class ProviderOrderController {

    @Autowired private RepeatedCode repeatedCode;

    @Autowired private OrderRepository orderRepository;

    @Autowired private ProviderOrderService providerOrderService;

    private static final Logger logger = LoggerFactory.getLogger(ProviderOrderController.class);

    // http://localhost:8080/provider/orders/pending
    @GetMapping("/pending")
    public ResponseEntity<List<PendingOrderDTO>> getPendingOrders(HttpServletRequest request) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(providerOrderService.getPendingOrdersForServiceProvider(userId));
    }

    // http://localhost:8080/provider/orders/{orderId}/accept
    @PutMapping("/{orderId}/accept")
    public ResponseEntity<String> acceptPendingOrder(HttpServletRequest request,
                                                     @PathVariable Long orderId) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(providerOrderService.acceptOrder(userId, orderId));
    }

    // http://localhost:8080/provider/orders/{orderId}/reject
    @PutMapping("/{orderId}/reject")
    public ResponseEntity<String> rejectPendingOrder(HttpServletRequest request,
                                                     @PathVariable Long orderId) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(providerOrderService.rejectOrder(userId, orderId));
    }

    // http://localhost:8080/provider/orders/active
    @GetMapping("/active")
    public ResponseEntity<List<PendingOrderDTO>> getActiveOrders(HttpServletRequest request) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(providerOrderService.getActiveOrdersForServiceProvider(userId));
    }

    // http://localhost:8080/provider/orders/{orderId}/inprocess
    @PutMapping("/{orderId}/inprocess")
    public ResponseEntity<String> markInProgress(@PathVariable Long orderId, HttpServletRequest request) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not exist."));

        if(order.getStatus() != OrderStatus.ACCEPTED_BY_PROVIDER) {
            logger.error("Order is not accepted yet or completed already.");
            return ResponseEntity.ok("Order is not accepted yet or completed already.");
        }

        return ResponseEntity.ok(providerOrderService.sendOTP(userId, order, OtpPurpose.IN_PROCESS));
    }

    // http://localhost:8080/provider/orders/{orderId}/inprocess/verify-otp
    @PutMapping("/{orderId}/inprocess/verify-otp")
    public ResponseEntity<String> verifyInProcessOtp(HttpServletRequest request,
                                                     @PathVariable Long orderId,
                                                     @RequestBody Map<String, String> otpCode) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(providerOrderService.verifyInProcessOtp(orderId, otpCode.get("otp")));
    }

    // http://localhost:8080/provider/orders/{orderId}/complete
    @PutMapping("/{orderId}/complete")
    public ResponseEntity<String> markCompleted(@PathVariable Long orderId, HttpServletRequest request) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not exist."));

        if(order.getStatus() != OrderStatus.IN_PROCESS) {
            logger.error("Order is not in process yet or not accepted yet.");
            return ResponseEntity.ok("Order is not in process yet or not accepted yet.");
        }

        return ResponseEntity.ok(providerOrderService.sendOTP(userId, order, OtpPurpose.COMPLETED));
    }

    // http://localhost:8080/provider/orders/{orderId}/complete/verify-otp
    @PutMapping("/{orderId}/complete/verify-otp")
    public ResponseEntity<String> verifyCompleteOtp(HttpServletRequest request,
                                                    @PathVariable Long orderId,
                                                    @RequestBody Map<String, String> otpCode) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(providerOrderService.verifyCompleteOtp(orderId, otpCode.get("otp")));
    }

    // http://localhost:8080/provider/orders/delivered
    @GetMapping("/delivered")
    public ResponseEntity<List<PendingOrderDTO>> getDeliveredOrders(HttpServletRequest request) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(providerOrderService.getDeliveredOrdersForServiceProvider(userId));
    }

}


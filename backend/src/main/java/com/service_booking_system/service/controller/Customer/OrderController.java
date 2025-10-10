package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.controller.Admin.RepeatedCode;
import com.service_booking_system.service.dto.Customer.OrderRequestDTO;
import com.service_booking_system.service.dto.Customer.OrderResponseDto;
import com.service_booking_system.service.model.Orders;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.Customer.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    @Autowired
    private  UserRepository usersRepository;
    @Autowired
    private RepeatedCode repeatedCode;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/book")
    public ResponseEntity<Orders> bookOrder(
            HttpServletRequest request,
            @RequestBody OrderRequestDTO dto
    ) {
        // 1️⃣ Extract userId from JWT token
        long userId = repeatedCode.fetchUserIdFromToken(request);

        // 2️⃣ Fetch the user (optional: check if blocked)
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (user.isBlocked()) {
            throw new AccessDeniedException("Your account is blocked by admin. Cannot book order.");
        }

        // 3️⃣ Use userId from token, ignore client-provided userId
        Orders savedOrder = orderService.bookOrder(dto, userId, dto.getServiceProviderId());
        return ResponseEntity.ok(savedOrder);
    }


    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDto>> getOrdersForLoggedInUser(
            HttpServletRequest request
    ) {
        // 1️⃣ Extract userId from JWT token
        long userId = repeatedCode.fetchUserIdFromToken(request);

        // 2️⃣ Fetch the user (optional)
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        if (user.isBlocked()) {
            throw new AccessDeniedException("Your account is blocked by admin. Cannot fetch orders.");
        }

        // 3️⃣ Fetch orders for this user
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

}


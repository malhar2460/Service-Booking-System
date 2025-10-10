package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.dto.Customer.OrderRequestDTO;
import com.service_booking_system.service.dto.Customer.OrderResponseDto;
import com.service_booking_system.service.model.Orders;
import com.service_booking_system.service.service.Customer.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/book")
    public ResponseEntity<Orders> bookOrder(@RequestBody OrderRequestDTO dto) {
        Orders savedOrder = orderService.bookOrder(dto, dto.getUserId(), dto.getServiceProviderId());
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}


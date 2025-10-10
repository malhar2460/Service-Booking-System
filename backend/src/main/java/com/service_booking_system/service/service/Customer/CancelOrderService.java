package com.service_booking_system.service.service.Customer;

import com.service_booking_system.service.enums.OrderStatus;
import com.service_booking_system.service.model.Orders;
import com.service_booking_system.service.model.ServiceProvider;
import com.service_booking_system.service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancelOrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final EmailService emailService;
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        // 1. Fetch the order by orderId and userId to verify ownership and existence
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        // Optionally verify the order belongs to this user
        if (!order.getUsers().getUserId().equals(userId)) {
            throw new SecurityException("User is not authorized to cancel this order");
        }

        // 2. Check if order is already cancelled or completed
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new IllegalStateException("Completed orders cannot be cancelled");
        }

        // 3. Update order status to CANCELLED
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        // 4. Notify service provider
        ServiceProvider sp = order.getServiceProvider();
        String message = "Order " + order.getOrderId() + " from user " + userId + " has been cancelled.";

        //smsService.sendOrderStatusNotification(sp.getUser().getPhoneNo(), message);
        emailService.sendOrderStatusNotification(
                sp.getUser().getEmail(),
                "Order Cancelled Notification",
                message
        );
    }

}

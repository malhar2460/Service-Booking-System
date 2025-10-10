package com.service_booking_system.service.service.Customer;

import com.service_booking_system.service.dto.Customer.FeedbackRequestDto;
import com.service_booking_system.service.model.Feedback;
import com.service_booking_system.service.model.Orders;
import com.service_booking_system.service.model.ServiceProvider;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.FeedbackRepository;
import com.service_booking_system.service.repository.OrderRepository;
import com.service_booking_system.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final FeedbackRepository feedbackRepository;
    public void submitFeedbackProviders(Long userId, FeedbackRequestDto dto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Orders order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Ensure the order belongs to this user
        if (!order.getUsers().getUserId().equals(userId)) {
            throw new RuntimeException("You are not allowed to give feedback on this order");
        }

        ServiceProvider provider = order.getServiceProvider();
        if (provider == null) {
            throw new RuntimeException("Service Provider is not assigned to this order.");
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setOrder(order);
        feedback.setServiceProvider(provider);
        feedback.setRating(dto.getRating());
        feedback.setReview(dto.getReview());

        feedbackRepository.save(feedback);
    }

}

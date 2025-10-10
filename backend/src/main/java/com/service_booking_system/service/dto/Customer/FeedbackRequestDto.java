package com.service_booking_system.service.dto.Customer;

import lombok.Data;

@Data
public class FeedbackRequestDto {
    private String serviceProviderId;
    private Integer rating;
    private String review;
    private Long orderId;
}

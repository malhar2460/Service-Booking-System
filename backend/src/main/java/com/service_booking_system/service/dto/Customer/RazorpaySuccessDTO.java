package com.service_booking_system.service.dto.Customer;

import lombok.Data;

@Data
public class RazorpaySuccessDTO {
    private String paymentId;
    private Long invoiceNumber;
    private String method;
}

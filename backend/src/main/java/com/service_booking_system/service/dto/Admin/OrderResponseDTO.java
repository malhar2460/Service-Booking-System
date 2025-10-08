package com.service_booking_system.service.dto.Admin;

import com.service_booking_system.service.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private String customerName;
    private String providerName;
    private String subServiceName;
    private OrderResponseDTO.BillDTO billDTO;
    private OrderResponseDTO.PaymentDTO paymentDTO;
    private OrderResponseDTO.PayoutDTO payoutDTOS;
    private AdminRevenueDTO adminRevenueDTO;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BillDTO {
        private Long invoiceNumber;
        private Double charge;
        private Double gst;
        private Double total;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PaymentDTO{
        private Long paymentId;
        private String transactionId;
        private LocalDateTime dateTime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PayoutDTO{
        private Long payoutId;
        private Double serviceEarning;
        private Double charge;
        private Double finalAmount;
        private String transactionId;
        private PaymentStatus payoutStatus;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminRevenueDTO{
        private Double profit;
    }
}


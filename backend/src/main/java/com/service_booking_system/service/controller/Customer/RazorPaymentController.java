package com.service_booking_system.service.controller.Customer;


import com.service_booking_system.service.dto.Customer.RazorpaySuccessDTO;
import com.service_booking_system.service.model.Bill;
import com.service_booking_system.service.enums.BillStatus;
import com.service_booking_system.service.model.Payment;
import com.service_booking_system.service.enums.PaymentStatus;
import com.service_booking_system.service.repository.BillRepository;
import com.service_booking_system.service.repository.PaymentRepository;
import com.service_booking_system.service.service.Admin.PayoutAssignmentService;
import com.service_booking_system.service.service.Customer.RazorpayService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class RazorPaymentController {

    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;
    private final RazorpayService razorpayService;
    private final PayoutAssignmentService payoutAssignmentService;

    @PostMapping("/create/{invoiceNumber}")
    public ResponseEntity<?> createOrder(@PathVariable Long invoiceNumber) {
        Bill bill = billRepository.findById(invoiceNumber)
                .orElseThrow(() -> new RuntimeException("Invalid invoice number"));

        String orderId = razorpayService.createRazorpayOrder(bill);
        return ResponseEntity.ok(Map.of("orderId", orderId, "amount", bill.getTotal() * 100));
    }

    @PostMapping("/success")
    @Transactional
    public ResponseEntity<?> paymentSuccess(@RequestBody RazorpaySuccessDTO dto) {
        System.out.println("Received Razorpay success payload: " + dto);

        if (dto.getInvoiceNumber() == null || dto.getPaymentId() == null || dto.getMethod() == null) {
            return ResponseEntity.badRequest().body("Missing required payment data");
        }

        Bill bill = billRepository.findById(dto.getInvoiceNumber())
                .orElseThrow(() -> new RuntimeException("Bill not found: " + dto.getInvoiceNumber()));

        // Save payment
        Payment payment = Payment.builder()
                .bill(bill)
                .transactionId(dto.getPaymentId())
                .method(dto.getMethod())
                .dateTime(LocalDateTime.now())
                .status(PaymentStatus.PAID)
                .build();
        paymentRepository.save(payment);

        bill.setStatus(BillStatus.PAID);
        billRepository.save(bill);


        billRepository.save(bill);

        // Trigger payouts
        //payoutAssignmentService.addPayouts(payment);

        return ResponseEntity.ok("Payment recorded and payouts added");
    }



}

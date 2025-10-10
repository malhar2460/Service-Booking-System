package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.repository.BillRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillRepository billRepository;

    public BillController(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getBillByOrderId(@PathVariable Long orderId) {
        return billRepository.findByOrder_OrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     *  Get bill by invoice number (optional endpoint)
     */
    @GetMapping("/invoice/{invoiceNumber}")
    public ResponseEntity<?> getBillByInvoiceNumber(@PathVariable Long invoiceNumber) {
        return billRepository.findById(invoiceNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


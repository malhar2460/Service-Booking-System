package com.service_booking_system.service.service.Customer;
import com.service_booking_system.service.model.Bill;
import com.service_booking_system.service.model.Orders;
import com.service_booking_system.service.repository.BillRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    private final BillRepository billRepository;

    public String createRazorpayOrder(Bill bill) {
        try {
            //  Initialize Razorpay client
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

            //  Create order options JSON
            JSONObject options = new JSONObject();
            options.put("amount", (int) Math.round(bill.getTotal() * 100)); // Razorpay accepts amount in paise
            options.put("currency", "INR");
            options.put("receipt", "INV-" + bill.getInvoiceNumber());
            options.put("payment_capture", 1); // auto capture

            //  Create order on Razorpay
            Order order = razorpay.orders.create(options);

            return order.get("id");
        } catch (Exception e) {
            throw new RuntimeException(" Razorpay order creation failed: " + e.getMessage(), e);
        }
    }
}


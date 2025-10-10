package com.service_booking_system.service.service.Customer;

import com.service_booking_system.service.dto.Customer.OrderRequestDTO;
import com.service_booking_system.service.dto.Customer.OrderResponseDto;
import com.service_booking_system.service.enums.BillStatus;
import com.service_booking_system.service.model.*;
import com.service_booking_system.service.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository ordersRepository;
    private final UserRepository usersRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final PriceRepository pricesRepository;
    private final BillRepository billRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    public OrderService(OrderRepository ordersRepository,
                        UserRepository usersRepository,
                        ServiceProviderRepository serviceProviderRepository,
                        PriceRepository pricesRepository,
                        BillRepository billRepository) {
        this.ordersRepository = ordersRepository;
        this.usersRepository = usersRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.pricesRepository = pricesRepository;
        this.billRepository = billRepository;
    }

    @Transactional
    public Orders bookOrder(OrderRequestDTO dto, Long userId, Long serviceProviderId) {
        // ✅ Fetch user
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Fetch service provider
        ServiceProvider serviceProvider = serviceProviderRepository.findById(serviceProviderId)
                .orElseThrow(() -> new RuntimeException("Service provider not found"));

        // ✅ Fetch price
        Prices price = pricesRepository.findBySubServices_SubServiceId(dto.getSubServiceId())
                .orElseThrow(() -> new RuntimeException("Price for subservice not found"));

        // ✅ Build order
        Orders order = Orders.builder()
                .users(user)
                .serviceProvider(serviceProvider)
                .price(price)
                .contactName(dto.getContactName())
                .contactPhone(dto.getContactPhone())
                .contactAddress(dto.getContactAddress())
                .date(LocalDate.parse(dto.getDate()))
                .time(LocalTime.parse(dto.getTime()))
                .build();

        // ✅ Save order in DB
        Orders savedOrder = ordersRepository.save(order);

        // ✅ Create Bill for the order
        double charge = savedOrder.getPrice().getAmount(); // from Price model
        double gst = charge * 0.03; // 3% GST
        double total = charge + gst;

        Bill bill = Bill.builder()
                .order(savedOrder)
                .charge(charge)
                .gst(gst)
                .total(total)
                .status(BillStatus.PENDING)
                .build();

        billRepository.save(bill);

        // ✅ Send email notification to service provider
        try {
            if (savedOrder.getServiceProvider() != null && savedOrder.getServiceProvider().getUser() != null) {
                String providerEmail = savedOrder.getServiceProvider().getUser().getEmail();
                String customerName = savedOrder.getUsers().getFirstName();

                emailService.sendOrderStatusNotification(
                        providerEmail,
                        "New Laundry Order Request",
                        "You have received a new laundry order from " + customerName + "."
                );
            }
        } catch (Exception e) {
            System.out.println("⚠️ Failed to send order notification email: " + e.getMessage());
        }

        return savedOrder;
    }

    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        List<Orders> orders = ordersRepository.findByUsers_UserId(userId);

        return orders.stream().map(order -> new OrderResponseDto(
                order.getOrderId(),
                order.getContactName(),
                order.getContactPhone(),
                order.getContactAddress(),
                order.getDate(),
                order.getTime(),
                order.getStatus().toString(),
                order.getServiceProvider() != null ? order.getServiceProvider().getBusinessName() : null
        )).collect(Collectors.toList());
    }
}

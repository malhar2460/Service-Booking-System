package com.service_booking_system.service.service.Service_Provider;

import com.service_booking_system.service.dto.Service_Provider.PendingOrderDTO;
import com.service_booking_system.service.enums.OrderStatus;
import com.service_booking_system.service.enums.OtpPurpose;
import com.service_booking_system.service.model.*;
import com.service_booking_system.service.repository.*;
import com.service_booking_system.service.service.Customer.EmailService;
import com.service_booking_system.service.service.Customer.OTPService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProviderOrderService {

    @Autowired private ServiceProviderRepository serviceProviderRepository;

    @Autowired private OrderRepository orderRepository;

    @Autowired private EmailService emailService;

    @Autowired private OTPService otpService;

    @Autowired private OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Autowired private UserRepository userRepository;

    @Autowired private OrderOtpRepository orderOtpRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProviderOrderService.class);

    @Transactional
    public List<PendingOrderDTO> getPendingOrdersForServiceProvider(long userId) {

        ServiceProvider serviceProvider = serviceProviderRepository.findByUserUserId(userId);
        List<PendingOrderDTO> pendingOrderList = new ArrayList<PendingOrderDTO>();

        if(serviceProvider != null) {
            List<Orders> pendingOrders = orderRepository.findByServiceProviderAndStatus(serviceProvider, OrderStatus.PENDING);

            for(Orders pendingOrder: pendingOrders) {

                PendingOrderDTO order = PendingOrderDTO.builder()
                        .orderId(pendingOrder.getOrderId())
                        .date(pendingOrder.getDate())
                        .time(pendingOrder.getTime())
                        .contactName(pendingOrder.getContactName())
                        .contactPhone(pendingOrder.getContactPhone())
                        .contactAddress(pendingOrder.getContactAddress())
                        .serviceName(pendingOrder.getPrice().getSubServices().getServices().getServiceName())
                        .subServiceName(pendingOrder.getPrice().getSubServices().getSubServiceName())
                        .charge(pendingOrder.getPrice().getAmount())
                        .build();

                pendingOrderList.add(order);
            }

        } else  {
            logger.error("Service provider not exist.");
            throw new UsernameNotFoundException("Service provider not exist.");
        }

        return pendingOrderList;
    }

    @Transactional
    private void saveOrderStatusHistory(Orders order, OrderStatus status) {
        orderStatusHistoryRepository.save(OrderStatusHistory.builder()
                .order(order)
                .status(status)
                .changedAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public String acceptOrder(long userId, Long orderId) {

        ServiceProvider serviceProvider = serviceProviderRepository.findByUserUserId(userId);

        try {
            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalStateException("Order with ID " + orderId + " not found"));


            if (!order.getServiceProvider().getServiceProviderId().equals(serviceProvider.getServiceProviderId())) {
                logger.error("Order does not belong to logged-in service provider");
                throw new IllegalStateException("Order does not belong to logged-in service provider");
            }

            if (order.getStatus() != OrderStatus.PENDING) {
                logger.error("Order is not in PENDING status and cannot be accepted");
                throw new IllegalStateException("Order is not in PENDING status and cannot be accepted");
            }

            // Update order status
            order.setStatus(OrderStatus.ACCEPTED_BY_PROVIDER);
            order = orderRepository.save(order);

            saveOrderStatusHistory(order, OrderStatus.ACCEPTED_BY_PROVIDER);

            // Send notifications
            emailService.sendOrderStatusNotification(order.getUsers().getEmail(),
                    "Order Accepted",
                    "Your Order " + order.getOrderId() + " is Accepted");

            logger.info("Order {} accepted by service provider {} for customer {}",
                    order.getOrderId(), serviceProvider.getBusinessName(), order.getUsers().getUserId());

            return "Order " + order.getOrderId()  + " accepted by service provider " + serviceProvider.getBusinessName()
                    + " for customer " + order.getUsers().getUserId();

        } catch (Exception e) {
            logger.error("Failed to accept order {} by service provider {}: {}",
                    orderId, serviceProvider.getBusinessName(), e.getMessage(), e);
            throw new RuntimeException("Failed to accept order: " + e.getMessage());
        }
    }

    @Transactional
    public String rejectOrder(long userId, Long orderId) {

        ServiceProvider serviceProvider = serviceProviderRepository.findByUserUserId(userId);

        try {
            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalStateException("Order with ID " + orderId + " not found"));

            if (!order.getServiceProvider().getServiceProviderId().equals(serviceProvider.getServiceProviderId())) {
                logger.error("Order does not belong to logged-in service provider");
                throw new IllegalStateException("Order does not belong to logged-in service provider");
            }

            if (order.getStatus() != OrderStatus.PENDING) {
                logger.error("Order is not in PENDING status and cannot be rejected");
                throw new IllegalStateException("Order is not in PENDING status and cannot be rejected");
            }

//            order.setStatus(OrderStatus.REJECTED_BY_PROVIDER);
            orderRepository.save(order);

//            saveOrderStatusHistory(order, OrderStatus.REJECTED_BY_PROVIDER);

            emailService.sendOrderStatusNotification(order.getUsers().getEmail(),
                    "Order Rejected",
                    "We're sorry! Your order " + order.getOrderId() + " has been rejected.");

            logger.info("Order {} rejected by service provider {} for customer {}",
                    order.getOrderId(), serviceProvider.getBusinessName(), order.getUsers().getUserId());

            return "Order " + order.getOrderId()  + " rejected by service provider " + serviceProvider.getBusinessName()
                    + " for customer " + order.getUsers().getUserId();

        } catch (Exception e) {
            logger.error("Failed to reject order {} by service provider {}: {}", orderId, userId, e.getMessage(), e);
            throw new RuntimeException("Failed to reject order: " + e.getMessage());
        }
    }

    @Transactional
    public List<PendingOrderDTO> getActiveOrdersForServiceProvider(long userId) {

        ServiceProvider serviceProvider = serviceProviderRepository.findByUserUserId(userId);

        List<PendingOrderDTO> activeOrdersList = new ArrayList<PendingOrderDTO>();

        if(serviceProvider != null) {

            List<Orders> activeOrders = orderRepository.findByServiceProviderAndStatus(serviceProvider, OrderStatus.IN_PROCESS);

            for(Orders activeOrder: activeOrders) {

                PendingOrderDTO order = PendingOrderDTO.builder()
                        .orderId(activeOrder.getOrderId())
                        .date(activeOrder.getDate())
                        .time(activeOrder.getTime())
                        .contactName(activeOrder.getContactName())
                        .contactPhone(activeOrder.getContactPhone())
                        .contactAddress(activeOrder.getContactAddress())
                        .serviceName(activeOrder.getPrice().getSubServices().getServices().getServiceName())
                        .subServiceName(activeOrder.getPrice().getSubServices().getSubServiceName())
                        .charge(activeOrder.getPrice().getAmount())
                        .build();

                activeOrdersList.add(order);
            }

        } else  {
            logger.error("Service provider not exist.");
            throw new UsernameNotFoundException("Service provider not exist.");
        }

        return activeOrdersList;
    }

    @Transactional
    public List<PendingOrderDTO> getDeliveredOrdersForServiceProvider(long userId) {

        ServiceProvider serviceProvider = serviceProviderRepository.findByUserUserId(userId);

        List<PendingOrderDTO> completedOrdersList = new ArrayList<PendingOrderDTO>();

        if(serviceProvider != null) {

            List<Orders> completedOrders = orderRepository.findByServiceProviderAndStatus(serviceProvider, OrderStatus.COMPLETED);

            for(Orders completedOrder: completedOrders) {

                PendingOrderDTO order = PendingOrderDTO.builder()
                        .orderId(completedOrder.getOrderId())
                        .date(completedOrder.getDate())
                        .time(completedOrder.getTime())
                        .contactName(completedOrder.getContactName())
                        .contactPhone(completedOrder.getContactPhone())
                        .contactAddress(completedOrder.getContactAddress())
                        .serviceName(completedOrder.getPrice().getSubServices().getServices().getServiceName())
                        .subServiceName(completedOrder.getPrice().getSubServices().getSubServiceName())
                        .charge(completedOrder.getPrice().getAmount())
                        .build();

                completedOrdersList.add(order);
            }

        } else  {
            logger.error("Service provider not exist.");
            throw new UsernameNotFoundException("Service provider not exist.");
        }

        return completedOrdersList;
    }

    public String sendOTP(long userId, Orders order, OtpPurpose purpose) {

        Users user = userRepository.findById(order.getUsers().getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Customer not exist."));

        ServiceProvider serviceProvider = serviceProviderRepository.findByUserUserId(userId);

        String otpKey = user.getEmail() != null ? user.getEmail() : user.getPhoneNo();
        String otp = otpService.generateOtp(otpKey);

        if (user.getEmail() != null) {
            emailService.sendOtp(user.getEmail(), otp);
        }

        OrderOtp orderOtp = OrderOtp.builder()
                .order(order)
                .user(user)
                .serviceProvider(serviceProvider)
                .generatedAt(LocalDateTime.now())
                .isUsed(false)
                .purpose(purpose)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .otpCode(otp)
                .build();

        orderOtpRepository.save(orderOtp);

        logger.info("Otp successfully send to customer. Verify Now");
        return "Otp successfully send to customer. Verify Now";
    }

    public String verifyInProcessOtp(Long orderId, String otp) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!validateOtp(order, otp, OtpPurpose.IN_PROCESS)) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        order.setStatus(OrderStatus.IN_PROCESS);
        orderRepository.save(order);

        saveOrderStatusHistory(order, OrderStatus.IN_PROCESS);

        logger.info("Status is changed to IN_PROCESS.");
        return "Status is changed to IN_PROCESS.";
    }

    public String verifyCompleteOtp(Long orderId, String otp) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!validateOtp(order, otp, OtpPurpose.COMPLETED)) {
            throw new IllegalArgumentException("Invalid or expired OTP");
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        saveOrderStatusHistory(order, OrderStatus.COMPLETED);

        logger.info("Status is changed to COMPLETED.");
        return "Status is changed to COMPLETED.";
    }

    @Transactional
    public boolean validateOtp(Orders order, String inputOtp, OtpPurpose purpose) {
        OrderOtp otp = orderOtpRepository.findTopByOrderAndPurposeAndIsUsedFalseOrderByGeneratedAtDesc(order, purpose);

        if (otp == null) {
            logger.error("No valid OTP found: maybe already used or none generated yet.");
            return false;
        }

        System.out.printf("🔍 OTP match attempt for order=%s, purpose=%s, input=%s, dbOtp=%s, expiresAt=%s%n",
                order.getOrderId(), purpose, inputOtp, otp.getOtpCode(), otp.getExpiresAt());

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            logger.error("OTP expired.");
            return false;
        }

        if (!otp.getOtpCode().equals(inputOtp)) {
            logger.error("OTP does not match.");
            return false;
        }

        otp.setIsUsed(true);
        orderOtpRepository.save(otp);

        logger.info("OTP verified and marked as used.");
        return true;
    }

}


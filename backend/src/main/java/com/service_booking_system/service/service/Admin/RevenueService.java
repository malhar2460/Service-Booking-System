package com.service_booking_system.service.service.Admin;

import com.service_booking_system.service.dto.Admin.*;
import com.service_booking_system.service.model.*;
import com.service_booking_system.service.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class RevenueService {

    @Autowired private RevenueRepository revenueRepository;

    @Autowired private BillRepository billRepository;

    @Autowired private PaymentRepository paymentRepository;

    @Autowired private ServiceProviderRepository serviceProviderRepository;

    @Autowired private AdminRevenueRepository adminRevenueRepository;

    @Autowired private OrderRepository orderRepository;

    @Autowired private PayoutRepository payoutRepository;

    private static final Logger logger = LoggerFactory.getLogger(RevenueService.class);

    public RevenueResponseDTO getSummary(String filter, LocalDate startDate, LocalDate endDate) {

        LocalDateTime today = LocalDateTime.now();
        LocalDateTime start, end;

        Double totalRevenue, grossSales, providerPayouts, avgOrderValue;
        Long totalOrders;

        switch (filter.toLowerCase()) {
            case "today":
                start = LocalDate.now().atStartOfDay();
                end = today;
                totalRevenue = revenueRepository.findTotalRevenueByDateRange(start, end);
                totalOrders = revenueRepository.findTotalOrdersByDateRange(start, end);
                grossSales = revenueRepository.findGrossSalesByDateRange(start, end);
                providerPayouts = revenueRepository.findPayoutsByDateRange(start, end);
                avgOrderValue = revenueRepository.findAverageOrderValueByDateRange(start, end);
                break;
            case "this week":
                start = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                end = today;
                totalRevenue = revenueRepository.findTotalRevenueByDateRange(start, end);
                totalOrders = revenueRepository.findTotalOrdersByDateRange(start, end);
                grossSales = revenueRepository.findGrossSalesByDateRange(start, end);
                providerPayouts = revenueRepository.findPayoutsByDateRange(start, end);
                avgOrderValue = revenueRepository.findAverageOrderValueByDateRange(start, end);
                break;
            case "this month":
                start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                end = today;
                totalRevenue = revenueRepository.findTotalRevenueByDateRange(start, end);
                totalOrders = revenueRepository.findTotalOrdersByDateRange(start, end);
                grossSales = revenueRepository.findGrossSalesByDateRange(start, end);
                providerPayouts = revenueRepository.findPayoutsByDateRange(start, end);
                avgOrderValue = revenueRepository.findAverageOrderValueByDateRange(start, end);
                break;
            case "custom":
                if (startDate == null || endDate == null) {
                    throw new IllegalArgumentException("Start and end date required for custom filter");
                }
                start = startDate.atStartOfDay();
                end = endDate.atTime(LocalTime.MAX);
                totalRevenue = revenueRepository.findTotalRevenueByDateRange(start, end);
                totalOrders = revenueRepository.findTotalOrdersByDateRange(start, end);
                grossSales = revenueRepository.findGrossSalesByDateRange(start, end);
                providerPayouts = revenueRepository.findPayoutsByDateRange(start, end);
                avgOrderValue = revenueRepository.findAverageOrderValueByDateRange(start, end);
                break;
            case "overall":
            default:
                start = LocalDate.now().withDayOfYear(1).atStartOfDay();
                end = today;
                totalRevenue = revenueRepository.findTotalRevenueByDateRange(start, end);
                totalOrders = revenueRepository.findTotalOrdersByDateRange(start, end);
                grossSales = revenueRepository.findGrossSalesByDateRange(start, end);
                providerPayouts = revenueRepository.findPayoutsByDateRange(start, end);
                avgOrderValue = revenueRepository.findAverageOrderValueByDateRange(start, end);
        }

        RevenueResponseDTO revenueResponseDTO = RevenueResponseDTO.builder()
                .totalRevenue(round(totalRevenue, 2))
                .totalOrders(totalOrders)
                .grossSales(round(grossSales, 2))
                .serviceProviderPayouts(round(providerPayouts, 2))
                .avgOrderValue(round(avgOrderValue, 2))
                .build();

        return revenueResponseDTO;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public List<TotalRevenueDTO> getTotalRevenue(String filter, LocalDate startDate, LocalDate endDate) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime start, end;

        switch (filter.toLowerCase()) {
            case "today":
                start = LocalDate.now().atStartOfDay();
                end = today;
                break;
            case "this week":
                start = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
                end = today;
                break;
            case "this month":
                start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                end = today;
                break;
            case "custom":
                if (startDate == null || endDate == null) {
                    throw new IllegalArgumentException("Start and end date required for custom filter");
                }
                start = startDate.atStartOfDay();
                end = endDate.atTime(LocalTime.MAX);
                break;
            case "overall":
            default:
                start = LocalDate.now().withDayOfYear(1).atStartOfDay();
                end = today;
        }

        List<TotalRevenueDTO> totalRevenueDTO = revenueRepository.getAdminRevenueBreakdownBetween(start, end);
        List<TotalRevenueDTO> resultList = new ArrayList<>();

        for(TotalRevenueDTO dto : totalRevenueDTO){

            TotalRevenueDTO revenueDTO = TotalRevenueDTO.builder()
                    .orderId(dto.getOrderId())
                    .date(dto.getDate())
                    .customerName(dto.getCustomerName())
                    .totalPaid(dto.getTotalPaid())
                    .providerPayout(round(dto.getProviderPayout(), 2))
                    .adminRevenue(round(dto.getAdminRevenue(), 2))
                    .build();

            resultList.add(revenueDTO);
        }
        return resultList;
    }

    public OrderResponseDTO getOrderDetail(Long orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not exists."));

        Bill bill = billRepository.findByOrder(order);

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();

        if(bill == null){
            orderResponseDTO.setBillDTO(null);
        } else {
            OrderResponseDTO.BillDTO billDTO = OrderResponseDTO.BillDTO.builder()
                    .invoiceNumber(bill.getInvoiceNumber())
                    .charge(bill.getCharge())
                    .gst(bill.getGst())
                    .total(bill.getTotal())
                    .build();

            orderResponseDTO.setBillDTO(billDTO);
        }

        Payment payment = paymentRepository.findByBill(bill);

        if(payment == null){
            orderResponseDTO.setPaymentDTO(null);
        } else {
            OrderResponseDTO.PaymentDTO paymentDTO = OrderResponseDTO.PaymentDTO.builder()
                    .paymentId(payment.getPaymentId())
                    .dateTime(payment.getDateTime())
                    .transactionId(payment.getTransactionId())
                    .build();

            orderResponseDTO.setPaymentDTO(paymentDTO);
        }

        Payout payout = payoutRepository.findByPayment(payment);

        if(payout == null){
            orderResponseDTO.setPayoutDTOS(null);
        } else {
            OrderResponseDTO.PayoutDTO payoutDTO = OrderResponseDTO.PayoutDTO.builder()
                    .payoutId(payout.getPayoutId())
                    .serviceEarning(payout.getServiceEarning())
                    .charge(payout.getCharge())
                    .finalAmount(payout.getFinalAmount())
                    .transactionId(payout.getTransactionId())
                    .payoutStatus(payout.getStatus())
                    .build();

            orderResponseDTO.setPayoutDTOS(payoutDTO);
        }

        AdminRevenue adminRevenue = adminRevenueRepository.findByPayment(payment);

        if(adminRevenue == null){
            orderResponseDTO.setAdminRevenueDTO(null);
        } else {
            OrderResponseDTO.AdminRevenueDTO adminRevenueDTO = OrderResponseDTO.AdminRevenueDTO.builder()
                    .profit(adminRevenue.getProfit())
                    .build();

            orderResponseDTO.setAdminRevenueDTO(adminRevenueDTO);
        }

        orderResponseDTO.setCustomerName(order.getUsers().getFirstName() + " " + order.getUsers().getLastName());
        orderResponseDTO.setProviderName(order.getServiceProvider().getBusinessName());
        orderResponseDTO.setSubServiceName(order.getPrice().getSubServices().getSubServiceName());

        return orderResponseDTO;
    }

    // Return revenue trend graph details
    public RevenueTrendDTO getRevenueTrendsGraph(String type, String filter) {
        LocalDateTime now = LocalDateTime.now();
        List<RevenueGraphPointDTO> graphPoints = new ArrayList<>();
        String title;
        LocalDateTime start;
        LocalDateTime end;

        logger.info("Type received in getRevenueTrend: {}", type);
        logger.info("Filter received in getRevenueTrend: {}", filter);

        switch (filter.toLowerCase()) {
            case "yearly" :
                title = "Yearly " + formatTypeTitle(type);
                for (int i = 1; i <= now.getMonthValue(); i++) {
                    start = LocalDate.of(now.getYear(), i, 1).atStartOfDay();
                    end = start.plusMonths(1);
                    String label = start.getMonth().name().substring(0, 3); // "Jan", "Feb", etc.
                    Double value = getRevenueByType(type, start, end);
                    graphPoints.add(new RevenueGraphPointDTO(label, value != null ? round(value, 2) : 0.0));
                }
                break;

            case "quarterly" :
                title = "Quarterly " + formatTypeTitle(type);
                YearMonth current = YearMonth.from(now);
                for (int i = 3; i >= 0; i--) {
                    YearMonth quarterMonth = current.minusMonths(i * 3L);
                    int year = quarterMonth.getYear();
                    int q = ((quarterMonth.getMonthValue() - 1) / 3) + 1;
                    start = LocalDate.of(year, ((q - 1) * 3 + 1), 1).atStartOfDay();
                    end = start.plusMonths(3);
                    String label = "Q" + q + " " + year;
                    Double value = getRevenueByType(type, start, end);
                    graphPoints.add(new RevenueGraphPointDTO(label, value != null ? round(value, 2) : 0.0));
                }
                break;

            case "monthly" :
            default :
                title = "Monthly " + formatTypeTitle(type);
                int currentYear = now.getYear();
                int currentMonth = now.getMonthValue();
                YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
                int daysInMonth = yearMonth.lengthOfMonth();

                for (int day = 1; day <= daysInMonth; day++) {
                    LocalDate date = LocalDate.of(currentYear, currentMonth, day);
                    start = date.atStartOfDay();
                    end = start.plusDays(1);

                    String label = String.format("%02d %s", day, date.getMonth().name().substring(0, 3)); // e.g., "01 Jun"
                    Double value = getRevenueByType(type, start, end);
                    graphPoints.add(new RevenueGraphPointDTO(label, value != null ? round(value, 2) : 0.0));
                }
                break;


        }

        return new RevenueTrendDTO(title, graphPoints);
    }

    private Double getRevenueByType(String type, LocalDateTime start, LocalDateTime end) {
        return switch (type.toLowerCase()) {
            case "gross sales" -> billRepository.getGrossSalesBetween(start, end);
            case "admin revenue" -> adminRevenueRepository.getAdminRevenueBetween(start, end);
            case "provider payout" -> payoutRepository.getProviderPayoutBetween(start, end);
            default -> throw new IllegalArgumentException("Invalid type: " + type);
        };
    }

    private String formatTypeTitle(String type) {
        return switch (type.toLowerCase()) {
            case "gross sales" -> "Gross Sales";
            case "admin revenue" -> "Admin Revenue";
            case "provider payout" -> "Provider Payout";
            case "delivery payout" -> "Delivery Payout";
            case "total payout" -> "Total Payouts";
            default -> "Revenue";
        };
    }

    public List<ServiceProviderRevenueTableDTO> getProviderRevenueTable(String filter) {

        logger.info("Filter received in getProviderRevenueTable: {}", filter);

        LocalDateTime start;
        LocalDateTime end;

        switch(filter.toLowerCase()){
            case "yearly" :
                start = LocalDate.now().withDayOfYear(1).atStartOfDay();
                end = LocalDateTime.now();
                break;
            case "quarterly":
                LocalDate today = LocalDate.now();
                LocalDate startDate = today.minusMonths(2).withDayOfMonth(1);
                start = startDate.atStartOfDay();
                end = LocalDateTime.now();
                break;
            case "monthly" :
            default :
                start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
                end = LocalDateTime.now();
        }

        logger.info("Date range for revenue query: {} to {}", start, end);

        List<ServiceProviderRevenueTableDTO> serviceProviderRevenueTableDTOS = payoutRepository
                .findAllProviderRevenuesInRange(start, end);

        logger.info("Fetched {} service provider revenue records", serviceProviderRevenueTableDTOS.size());

        // Optional: log a sample of the results
        serviceProviderRevenueTableDTOS.stream().limit(3).forEach(dto ->
                logger.info("Sample record → Provider ID: {}, Revenue: {}, Charge: {}",
                        dto.getProviderId(), dto.getTotalRevenue(), dto.getPlatformCharges())
        );

        // start and end date added to each result
        serviceProviderRevenueTableDTOS.forEach(dto -> {
            dto.setStartDate(start);
            dto.setEndDate(end);
        });

        return serviceProviderRevenueTableDTOS;
    }

    public RevenueTrendDTO getProviderRevenueGraph(String filter, Long providerId) {

        LocalDateTime now = LocalDateTime.now();
        List<RevenueGraphPointDTO> graphPoints = new ArrayList<>();
        String title;

        ServiceProvider serviceProvider = serviceProviderRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Service provider not found."));

        Long userId = serviceProvider.getServiceProviderId();

        switch (filter.toLowerCase()) {
            case "yearly":
                title = "Yearly Revenue for service provider : " + serviceProvider.getUser().getFirstName() + " " + serviceProvider.getUser().getLastName();
                for (int i = 1; i <= now.getMonthValue(); i++) {
                    LocalDateTime start = LocalDate.of(now.getYear(), i, 1).atStartOfDay();
                    LocalDateTime end = start.plusMonths(1);
                    String label = start.getMonth().name().substring(0, 3); // "Jan", "Feb", etc.
                    Double revenue = payoutRepository.getRevenueForUserInRange(userId, start, end);
                    graphPoints.add(new RevenueGraphPointDTO(label, revenue != null ? round(revenue, 2) : 0.0));
                }
                break;

            case "quarterly":
                title = "Quarterly Revenue for service provider : " + serviceProvider.getUser().getFirstName() + " " + serviceProvider.getUser().getLastName();
                YearMonth current = YearMonth.from(now);
                for (int i = 3; i >= 0; i--) {
                    YearMonth quarterMonth = current.minusMonths(i * 3L);
                    int q = ((quarterMonth.getMonthValue() - 1) / 3) + 1;
                    LocalDateTime start = LocalDate.of(quarterMonth.getYear(), ((q - 1) * 3 + 1), 1).atStartOfDay();
                    LocalDateTime end = start.plusMonths(3);
                    Double revenue = payoutRepository.getRevenueForUserInRange(userId, start, end);
                    graphPoints.add(new RevenueGraphPointDTO("Q" + q + " " + start.getYear(), revenue != null ? revenue : 0.0));
                }
                break;

            case "monthly":
            default:
                title = "Monthly Revenue for service provider : " + serviceProvider.getUser().getFirstName() + " " + serviceProvider.getUser().getLastName();
                int currentYear = now.getYear();
                int currentMonth = now.getMonthValue();
                YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
                int daysInMonth = yearMonth.lengthOfMonth();

                for (int day = 1; day <= daysInMonth; day++) {
                    LocalDate date = LocalDate.of(currentYear, currentMonth, day);
                    LocalDateTime start = date.atStartOfDay();
                    LocalDateTime end = start.plusDays(1);

                    String label = String.format("%02d %s", day, date.getMonth().name().substring(0, 3)); // e.g., "01 Jun"
                    Double revenue = payoutRepository.getRevenueForUserInRange(userId, start, end);
                    graphPoints.add(new RevenueGraphPointDTO(label, revenue != null ? round(revenue, 2) : 0.0));
                }
                break;
        }

        return RevenueTrendDTO.builder()
                .title(title)
                .data(graphPoints)
                .build();
    }

}


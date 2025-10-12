package com.service_booking_system.service.controller.Admin;

import com.service_booking_system.service.dto.Admin.*;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.service.Admin.RevenueService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

    @Autowired private RevenueService revenueService;

    @Autowired private RepeatedCode repeatedCode;

    // http://localhost:8080/revenue/summary
    // Return count of Total revenue, total orders, gross sales, service providers payouts, average order value.
    // Filter can use. Available filter values are today, this week, this month, custom(provide start and end date), overall(this year data)
    // Start and end date can be used to return revenue data. We can specify only one of them or both of them.
    @GetMapping("/summary")
    public ResponseEntity<RevenueResponseDTO> getRevenueSummary(
            HttpServletRequest request,
            @RequestParam(defaultValue = "Overall") String filter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(revenueService.getSummary(filter, startDate, endDate));
    }

    // http://localhost:8080/revenue/total-revenue
    // Return order details with revenue.
    // Filter can use. Available filter values are today, this week, this month, custom(provide start and end date), overall(this year data)
    // Start and end date can be used to return revenue data. We can specify only one of them or both of them.
    @GetMapping("/total-revenue")
    public ResponseEntity<List<TotalRevenueDTO>> getTotalRevenue(
            HttpServletRequest request,
            @RequestParam(defaultValue = "Overall") String filter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(revenueService.getTotalRevenue(filter, startDate, endDate));
    }

    // http://localhost:8080/revenue/total-revenue/{orderId}
    // Return order details with revenue.
    @GetMapping("/total-revenue/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderDetail(
            HttpServletRequest request,
            @PathVariable Long orderId ) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(revenueService.getOrderDetail(orderId));
    }

    // http://localhost:8080/revenue/trends
    // Return graph for revenue trends for admin, gross sales, total payouts, service provider payouts
    // based on filter monthly, quarterly, yearly
    @GetMapping("/trends")
    public ResponseEntity<RevenueTrendDTO> getRevenueTrendsGraph(HttpServletRequest request,
                                                                 @RequestParam(defaultValue = "gross sales") String type,
                                                                 @RequestParam(defaultValue = "monthly") String filter) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(revenueService.getRevenueTrendsGraph(type, filter));
    }

    // http://localhost:8080/revenue/provider-analytics-list
    // Return revenue analytics list of service providers based on filter monthly, quarterly and yearly
    @GetMapping("/provider-analytics-list")
    public ResponseEntity<List<ServiceProviderRevenueTableDTO>> getServiceProviderRevenueTable(
            HttpServletRequest request,
            @RequestParam(defaultValue = "monthly") String filter) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(revenueService.getProviderRevenueTable(filter));
    }

    // http://localhost:8080/revenue/provider-analytics-list/{providerId}
    // Return revenue analytics graph for each service provider
    @GetMapping("/provider-analytics-list/{providerId}")
    public ResponseEntity<RevenueTrendDTO> getServiceProviderRevenueGraph(
            HttpServletRequest request,
            @PathVariable Long providerId,
            @RequestParam(defaultValue = "Overall") String filter) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(revenueService.getProviderRevenueGraph(filter, providerId));
    }

}

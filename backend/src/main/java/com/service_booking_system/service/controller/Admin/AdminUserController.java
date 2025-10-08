// /backend/src/main/java/com/service_booking_system/service/controller/Admin/AdminUserController.java

package com.service_booking_system.service.controller.Admin;

import com.service_booking_system.service.dto.Admin.CustomerResponseDTO;
import com.service_booking_system.service.dto.Admin.ServiceProviderResponseDTO;
import com.service_booking_system.service.enums.UserRoles;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.service.Admin.AdminUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
public class AdminUserController {

    @Autowired private AdminUserService adminUserService;

    @Autowired private RepeatedCode repeatedCode;

    // http://localhost:8080/users/customer/graphs
    // Return graph based overview of customers.
    @GetMapping("/customers/graphs")
    public ResponseEntity<?> getGraphBasedOverviewForCustomers(HttpServletRequest request) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(adminUserService.getGraphsForUsers(UserRoles.CUSTOMER));
    }

    // http://localhost:8080/users/customers/table
    // Return table based data of customers based on searches or filter.
    @GetMapping("/customers/table")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(
            HttpServletRequest request,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "id") String sortBy
    ) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        List<CustomerResponseDTO> customers = adminUserService.getFilteredCustomers(keyword, startDate, endDate, sortBy);
        return ResponseEntity.ok(customers);
    }

    // http://localhost:8080/users/customers/table/block/{userId}
    // Block unblocked user.
    @PutMapping("/customers/table/block/{userId}")
    public ResponseEntity<String> blockCustomer(HttpServletRequest request, @PathVariable Long userId) throws AccessDeniedException {
        long adminId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(adminId);
        repeatedCode.isAdmin(user);
        adminUserService.toggleUserBlockStatus(userId, true);
        return ResponseEntity.ok("Customer blocked successfully");
    }

    // http://localhost:8080/users/customers/table/unblock/{userId}
    // Unblock blocked user.
    @PutMapping("/customers/table/unblock/{userId}")
    public ResponseEntity<String> unblockCustomer(HttpServletRequest request, @PathVariable Long userId) throws AccessDeniedException {
        long adminId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(adminId);
        repeatedCode.isAdmin(user);
        adminUserService.toggleUserBlockStatus(userId, false);
        return ResponseEntity.ok("Customer unblocked successfully");
    }

    // http://localhost:8080/users/customer/table/delete/{userId}
    // Delete user.
    @DeleteMapping("/customer/table/delete/{userId}")
    public ResponseEntity<String> deleteCustomer(HttpServletRequest request, @PathVariable Long userId) throws AccessDeniedException {
        long adminId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(adminId);
        repeatedCode.isAdmin(user);
        adminUserService.deleteCustomer(userId);
        return ResponseEntity.ok("Customer deleted successfully");
    }

    // http://localhost:8080/users/service-providers/graphs
    // Return graph based overview of service providers.
    @GetMapping("/service-providers/graphs")
    public ResponseEntity<?> getGraphBasedOverviewForServiceProvider(HttpServletRequest request) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(adminUserService.getGraphsForUsers(UserRoles.SERVICE_PROVIDER));
    }

    // http://localhost:8080/users/service-providers/table
    // Return table based data of service providers based on searches or filter.
    @GetMapping("/service-providers/table")
    public ResponseEntity<List<ServiceProviderResponseDTO>> getAllServiceProviders(
            HttpServletRequest request,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "id") String sortBy
    ) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        List<ServiceProviderResponseDTO> serviceProviders = adminUserService.getFilteredServiceProviders(keyword, startDate, endDate, sortBy);
        return ResponseEntity.ok(serviceProviders);
    }

    // http://localhost:8080/users/service-providers/table/block/{userId}
    // Block unblocked user.
    @PutMapping("/service-providers/table/block/{userId}")
    public ResponseEntity<String> blockServiceProvider(HttpServletRequest request, @PathVariable Long userId) throws AccessDeniedException {
        long adminId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(adminId);
        repeatedCode.isAdmin(user);
        adminUserService.toggleUserBlockStatus(userId, true);
        return ResponseEntity.ok("Service provider blocked successfully");
    }

    // http://localhost:8080/users/service-providers/table/unblock/{userId}
    // Unblock blocked user.
    @PutMapping("/service-providers/table/unblock/{userId}")
    public ResponseEntity<String> unblockServiceProvider(HttpServletRequest request, @PathVariable Long userId) throws AccessDeniedException {
        long adminId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(adminId);
        repeatedCode.isAdmin(user);
        adminUserService.toggleUserBlockStatus(userId, false);
        return ResponseEntity.ok("Service provider unblocked successfully");
    }

    // http://localhost:8080/users/service-providers/table/delete/{providerId}
    // Delete user.
    @DeleteMapping("/service-providers/table/delete/{providerId}")
    public ResponseEntity<String> deleteServiceProvider(HttpServletRequest request, @PathVariable Long providerId) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        adminUserService.deleteServiceProvider(providerId);
        return ResponseEntity.ok("Service provider deleted successfully");
    }

}


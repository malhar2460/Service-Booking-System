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

    // http://localhost:8080/users/customers/graphs
    // Return graph based overview of customers. Four type of graph data is return
    // a. customer growth based on months b. customer growth in current month
    // c. customer growth in last 5 year d. how many customers are from each city
    @GetMapping("/customers/graphs")
    public ResponseEntity<?> getGraphBasedOverviewForCustomers(HttpServletRequest request) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(adminUserService.getGraphsForUsers(UserRoles.CUSTOMER));
    }

    // http://localhost:8080/users/customers/table
    // Return table based data of customers based on searches or filter.
    // Return default data if filter not provided.(start date is 2025-01-01 and end date is current date and sort by user id)
    // Keywords can be used to search for particular customer either we can specify email id or phone no of cutomer to search.
    // Start and end date can be used to return customers joned between that date. We can specify only one of them or both of them.
    // We can also apply sort by filter along with date filter which can allow either sorting based on joinDate or user id.
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

    // http://localhost:8080/users/customers/table/block-unblock/{userId}
    // Toggle blocking. Block unblocked user or unblock blocked user.
    @PutMapping("/customers/table/block-unblock/{userId}")
    public ResponseEntity<String> toggleBlockingStatusOfCustomer(HttpServletRequest request, @PathVariable Long userId) throws AccessDeniedException {
        long adminId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(adminId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(adminUserService.toggleUserBlockStatus(userId));
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
    // Return graph based overview of service providers. (Four type of graph data is return
    // a. service provider growth based on months b. service provider growth in current month
    // c. service provider growth in last 5 year d. how many service provider are from each city )
    @GetMapping("/service-providers/graphs")
    public ResponseEntity<?> getGraphBasedOverviewForServiceProvider(HttpServletRequest request) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(adminUserService.getGraphsForUsers(UserRoles.SERVICE_PROVIDER));
    }

    // http://localhost:8080/users/service-providers/table
    // Return table based data of service providers based on searches or filter.
    // Return default data if filter not provided.(start date is 2025-01-01 and end date is current date and sort by user id)
    // Keywords can be used to search for particular customer either we can specify email id or phone no of cutomer to search.
    // Start and end date can be used to return customers joned between that date. We can specify only one of them or both of them.
    // We can also apply sort by filter along with date filter which can allow either sorting based on joinDate or user id.
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

    // http://localhost:8080/users/service-providers/table/block-unblock/{userId}
    // Toggle blocking. Block unblocked provider or unblock blocked provider.
    @PutMapping("/service-providers/table/block-unblock/{userId}")
    public ResponseEntity<String> toggleBlockingStatusOfProvider(HttpServletRequest request, @PathVariable Long userId) throws AccessDeniedException {
        long adminId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(adminId);
        repeatedCode.isAdmin(user);

        return ResponseEntity.ok(adminUserService.toggleUserBlockStatus(userId));
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


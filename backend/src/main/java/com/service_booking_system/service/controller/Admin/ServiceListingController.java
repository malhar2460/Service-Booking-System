// /backend/src/main/java/com/service_booking_system/service/controller/Admin/ServiceListingController.java

package com.service_booking_system.service.controller.Admin;

import com.service_booking_system.service.dto.Admin.ManageServiceListingRequestDTO;
import com.service_booking_system.service.dto.Admin.ServiceSummaryDTO;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.service.Admin.ServiceListingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/service")
public class ServiceListingController {

    @Autowired private ServiceListingService serviceListingService;

    @Autowired private RepeatedCode repeatedCode;

    // http://localhost:8080/service/summary
    // Return count of services
    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(HttpServletRequest request) {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        List<ServiceSummaryDTO> summary = serviceListingService.getServiceSummary();
        return ResponseEntity.ok(summary);
    }

    // http://localhost:8080/service/add-services
    // Render a form to submit services
    @PostMapping("/add-services")
    public ResponseEntity<String> addService(@Valid @RequestBody ManageServiceListingRequestDTO manageServiceListingRequestDTO,
                                             HttpServletRequest request) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(serviceListingService.addServiceDetails(user, manageServiceListingRequestDTO));
    }

    // http://localhost:8080/service/get-services
    @GetMapping("/get-services")
    public ResponseEntity<List<String>> getService(HttpServletRequest request)  {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(serviceListingService.getServices(userId));
    }

    // http://localhost:8080/service/add-subservices
    // Render a form to submit sub-service
    @PostMapping("/add-subservices")
    public ResponseEntity<String> addSubService(@Valid @RequestBody ManageServiceListingRequestDTO manageServiceListingRequestDTO,
                                                HttpServletRequest request) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(serviceListingService.addSubServiceDetails(user, manageServiceListingRequestDTO));
    }

    // http://localhost:8080/service/get-subservices/{serviceName}
    @GetMapping("/get-subservices/{serviceName}")
    public ResponseEntity<List<String>> getSubService(HttpServletRequest request, @PathVariable String serviceName)  {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        List<String> subServices = serviceListingService.getSubServiceNamesByServiceName(serviceName);
        return ResponseEntity.ok(subServices);
    }

}


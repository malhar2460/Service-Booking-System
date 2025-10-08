package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.dto.Customer.ServiceDTO;
import com.service_booking_system.service.service.Customer.ServicesService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/services")
public class ServicesController {

    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    // GET /api/services/all
    @GetMapping("/all")
    public List<ServiceDTO> getAllServices() {
        return servicesService.getAllServices();
    }
}

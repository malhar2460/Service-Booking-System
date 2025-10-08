package com.service_booking_system.service.controller.Customer;
import com.service_booking_system.service.dto.Customer.SubServiceDTO;
import com.service_booking_system.service.model.SubServices;
import com.service_booking_system.service.service.Customer.SubServicesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subservices")
public class SubServicesController {

    private final SubServicesService subServicesService;

    public SubServicesController(SubServicesService subServicesService) {
        this.subServicesService = subServicesService;
    }

    @GetMapping("/{serviceId}")
    public List<SubServiceDTO> getSubServicesWithPrice(@PathVariable Long serviceId) {
        return subServicesService.getSubServicesWithPrice(serviceId);
    }
}
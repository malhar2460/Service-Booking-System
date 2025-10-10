package com.service_booking_system.service.service.Customer;

import com.service_booking_system.service.dto.Customer.ServiceDTO;
import com.service_booking_system.service.repository.ServiceRepository;

import com.service_booking_system.service.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicesService {

    private final ServiceRepository servicesRepository;

    public ServicesService(ServiceRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    public List<ServiceDTO> getAllServices() {
        return servicesRepository.findAllServices();
    }
}

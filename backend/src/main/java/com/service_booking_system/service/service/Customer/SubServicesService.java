package com.service_booking_system.service.service.Customer;

import com.service_booking_system.service.dto.Customer.SubServiceDTO;
import com.service_booking_system.service.repository.SubServiceRepository;

import com.service_booking_system.service.repository.SubServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubServicesService {

    private final SubServiceRepository subServicesRepository;

    public SubServicesService(SubServiceRepository subServicesRepository) {
        this.subServicesRepository = subServicesRepository;
    }

    public List<SubServiceDTO> getSubServicesWithPrice(Long serviceId) {
        return subServicesRepository.findSubServicesWithPriceByServiceId(serviceId);
    }
}


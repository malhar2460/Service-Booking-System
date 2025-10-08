package com.service_booking_system.service.service.Customer;


import com.service_booking_system.service.dto.Customer.SubServiceDTO;
import com.service_booking_system.service.model.SubServices;
import com.service_booking_system.service.repository.SubServicesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubServicesService {

    private final SubServicesRepository subServicesRepository;

    public SubServicesService(SubServicesRepository subServicesRepository) {
        this.subServicesRepository = subServicesRepository;
    }

    public List<SubServiceDTO> getSubServicesWithPrice(Long serviceId) {
        return subServicesRepository.findSubServicesWithPriceByServiceId(serviceId);
    }
}


// /backend/src/main/java/com/service_booking_system/service/service/Admin/ServiceListingService.java

package com.service_booking_system.service.service.Admin;

import com.service_booking_system.service.dto.Admin.ManageServiceListingRequestDTO;
import com.service_booking_system.service.dto.Admin.ServiceSummaryDTO;
import com.service_booking_system.service.model.Services;
import com.service_booking_system.service.model.SubServices;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.ServiceRepository;
import com.service_booking_system.service.repository.SubServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceListingService {

    @Autowired private ServiceRepository serviceRepository;

    @Autowired private SubServiceRepository subServiceRepository;

    private static final Logger logger = LoggerFactory.getLogger(ServiceListingService.class);

    // Get summary
    public List<ServiceSummaryDTO> getServiceSummary() {
        List<Services> services = serviceRepository.findAll();

        return services.stream()
                .map(service -> {
                    long subServiceCount = subServiceRepository.countByServices(service);
                    return new ServiceSummaryDTO(
                            service.getServiceId(),
                            service.getServiceName(),
                            subServiceCount
                    );
                })
                .collect(Collectors.toList());
    }

    // Add service data
    public String addServiceDetails(Users users, ManageServiceListingRequestDTO manageServiceListingRequestDTO) throws AccessDeniedException {

        Services service = Services.builder()
                .serviceName(manageServiceListingRequestDTO.getServiceName())
                .user(users)
                .build();

        serviceRepository.save(service);

        logger.info("Service {} added successfully", manageServiceListingRequestDTO.getServiceName());
        return "Service " + manageServiceListingRequestDTO.getServiceName() + " added successfully";
    }

    // Fetch all services
    public List<String> getServices(long userId) {
        return serviceRepository.findAllServiceName();
    }

    // Add sub-service data
    public String addSubServiceDetails(Users users, ManageServiceListingRequestDTO manageServiceListingRequestDTO) throws AccessDeniedException {

        Services detachedService = serviceRepository.findByServiceName(manageServiceListingRequestDTO.getServiceName())
                .orElseThrow(() -> new RuntimeException("Service is not available."));

        // Ensure managed
//        Services managedService = serviceRepository.findById(detachedService.getServiceId())
//                .orElseThrow(() -> new RuntimeException("Service not found by ID."));

        SubServices subService = SubServices.builder()
//                .services(managedService)
                .services(detachedService)
                .subServiceName(manageServiceListingRequestDTO.getSubServiceName())
                .user(users)
                .build();

        subServiceRepository.save(subService);

        logger.info("Sub-service {} added successfully for service {}", manageServiceListingRequestDTO.getSubServiceName(), manageServiceListingRequestDTO.getServiceName());
        return "Sub-service " + manageServiceListingRequestDTO.getSubServiceName() + " added successfully for service " + manageServiceListingRequestDTO.getServiceName();
    }

    public List<String> getSubServiceNamesByServiceName(String serviceName) {
        Services service = serviceRepository.findByServiceName(serviceName)
                .orElseThrow(() -> new RuntimeException("Service not found: " + serviceName));

        List<SubServices> subServices = subServiceRepository.findByServices(service);
        return subServices.stream()
                .map(SubServices::getSubServiceName)
                .collect(Collectors.toList());
    }

}



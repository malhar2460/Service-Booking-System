package com.service_booking_system.service.service.Admin;

import com.service_booking_system.service.controller.Admin.RepeatedCode;
import com.service_booking_system.service.dto.Admin.ServiceProviderRequestDTO;
import com.service_booking_system.service.enums.Status;
import com.service_booking_system.service.model.Prices;
import com.service_booking_system.service.model.ServiceProvider;
import com.service_booking_system.service.model.UserAddress;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.PriceRepository;
import com.service_booking_system.service.repository.ServiceProviderRepository;
import com.service_booking_system.service.repository.UserAddressRepository;
import com.service_booking_system.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestService {

    @Autowired private UserRepository userRepository;

    @Autowired private UserAddressRepository userAddressRepository;

    @Autowired private ServiceProviderRepository serviceProviderRepository;

    @Autowired private PriceRepository priceRepository;

    @Autowired private RepeatedCode repeatedCode;

    // Return all service provider data which are pending for approval.
    @Transactional
    public List<ServiceProviderRequestDTO> getAllProviderProfiles() {

        List<ServiceProvider> serviceProviders = serviceProviderRepository.findByStatus(Status.PENDING);

        List<ServiceProviderRequestDTO> pendingProfiles = new ArrayList<>();

        for (ServiceProvider provider : serviceProviders) {

            if (provider == null) continue;

            // Fetch user
            Users user = userRepository.findById(provider.getUser().getUserId()).orElse(null);
            if (user == null) continue;

            // Fetch address
            UserAddress address = userAddressRepository.findByUser(user);

            ServiceProviderRequestDTO.AddressDTO addressDTO = null;
            if (address != null) {
                addressDTO = ServiceProviderRequestDTO.AddressDTO.builder()
                        .name(address.getName())
                        .areaName(address.getAreaName())
                        .pincode(address.getPincode())
                        .cityName(address.getCity().getCityName())
                        .build();
            }

            List<Prices> priceList = priceRepository.findByServiceProvider(provider);
            List<ServiceProviderRequestDTO.priceDTO> priceDTOList = new ArrayList<>();

            if(priceList != null) {

                ServiceProviderRequestDTO.priceDTO priceDTO = null;
                for (Prices price : priceList) {

                    priceDTO = ServiceProviderRequestDTO.priceDTO.builder()
                            .subServiceName(price.getSubServices().getSubServiceName())
                            .amount(price.getAmount())
                            .build();

                    priceDTOList.add(priceDTO);
                }
            }

            // Map final DTO
            ServiceProviderRequestDTO requestProfile = ServiceProviderRequestDTO.builder()
                    .userId(user.getUserId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phone(user.getPhoneNo())
                    .email(user.getEmail())
                    .businessName(provider.getBusinessName())
                    .businessLicenseNumber(provider.getBusinessLicenseNumber())
                    .gstNumber(provider.getGstNumber())
                    .addresses(addressDTO)
                    .aadharCardPhoto(provider.getAadharCardImage())
                    .panCardPhoto(provider.getPanCardImage())
                    .profilePhoto(provider.getProfileImage())
                    .businessUtilityBillPhoto(provider.getBusinessUtilityBillImage())
                    .bankName(provider.getBankName())
                    .ifscCode(provider.getIfscCode())
                    .bankAccountNumber(provider.getBankAccountNumber())
                    .accountHolderName(provider.getAccountHolderName())
                    .priceDTO(priceDTOList)
                    .build();

            pendingProfiles.add(requestProfile);
        }

        return pendingProfiles;

    }

    // Logic for accpet service provider request
    @Transactional
    public String acceptProvider(Long userId){

        Users user = repeatedCode.checkUser(userId);

        String email = user.getEmail();

        ServiceProvider serviceProvider = serviceProviderRepository.findByUser(user);
        if(serviceProvider == null){
            return "Service provider is not found.";
        }

        if(serviceProvider.getStatus() != Status.PENDING){
            return "Request for Service provider profile already accepted";
        } else {
            serviceProvider.setStatus(Status.ACCEPTED);
        }

        String message = "Congratulations! Your request to become a Service Provider has been approved.";
        String subject = "Service Provider Approval";

//        emailService.sendOrderStatusNotification(email, subject, message);

        return "Service provider profile approved successfully.";
    }

    // Logic for reject service provider request
    @Transactional
    public String rejectProvider(Long userId){

        Users user = repeatedCode.checkUser(userId);

        String email = user.getEmail();

        ServiceProvider serviceProvider = serviceProviderRepository.findByUser(user);

        if(serviceProvider.getStatus() != Status.REJECTED){
            return "Request for service provider profile already rejected.";
        } else {
            serviceProvider.setStatus(Status.REJECTED);
        }

        // Send SMS + Email notification
        String message = "We're sorry! Your request to become a Service Provider has been rejected.";
        String subject = "Service Provider Rejection";

//        emailService.sendOrderStatusNotification(email, subject, message);

        return "Service provider profile rejected.";

    }

}


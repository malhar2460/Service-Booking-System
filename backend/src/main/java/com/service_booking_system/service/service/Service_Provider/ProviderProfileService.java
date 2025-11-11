package com.service_booking_system.service.service.Service_Provider;

import com.service_booking_system.service.dto.Admin.ServiceProviderResponseDTO;
import com.service_booking_system.service.model.ServiceProvider;
import com.service_booking_system.service.model.UserAddress;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.ServiceProviderRepository;
import com.service_booking_system.service.repository.UserAddressRepository;
import com.service_booking_system.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProviderProfileService {

    @Autowired private UserRepository userRepository;

    @Autowired private UserAddressRepository userAddressRepository;

    @Autowired private ServiceProviderRepository serviceProviderRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProviderProfileService.class);

    @Transactional
    public ServiceProviderResponseDTO getServiceProviderProfileDetail(long userId) {

        // Fetch user
        Users user = userRepository.findById(userId).orElse(null);
        ServiceProviderResponseDTO.AddressDTO addressDTO = null;
        ServiceProviderResponseDTO requestProfile = new ServiceProviderResponseDTO();

        if (user != null) {

            // Fetch address
            UserAddress address = userAddressRepository.findByUser(user);

            if (address != null) {
                addressDTO = ServiceProviderResponseDTO.AddressDTO.builder()
                        .name(address.getName())
                        .areaName(address.getAreaName())
                        .pincode(address.getPincode())
                        .cityName(address.getCity().getCityName())
                        .build();
            } else {
                addressDTO = ServiceProviderResponseDTO.AddressDTO.builder()
                        .name(null)
                        .areaName(null)
                        .pincode(null)
                        .cityName(null)
                        .build();
            }

            ServiceProvider serviceProvider = serviceProviderRepository.findByUser(user);

            if(serviceProvider != null) {

                // Map final DTO
                requestProfile = ServiceProviderResponseDTO.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPhoneNo())
                        .email(user.getEmail())
                        .joinedAt(user.getCreatedAt())
                        .businessName(serviceProvider.getBusinessName())
                        .businessLicenseNumber(serviceProvider.getBusinessLicenseNumber())
                        .gstNumber(serviceProvider.getGstNumber())
                        .addresses(addressDTO)
                        .aadharCardPhoto(serviceProvider.getAadharCardImage())
                        .panCardPhoto(serviceProvider.getPanCardImage())
                        .profilePhoto(serviceProvider.getProfileImage())
                        .businessUtilityBillPhoto(serviceProvider.getBusinessUtilityBillImage())
                        .bankName(serviceProvider.getBankName())
                        .ifscCode(serviceProvider.getIfscCode())
                        .bankAccountNumber(serviceProvider.getBankAccountNumber())
                        .accountHolderName(serviceProvider.getAccountHolderName())
                        .build();

            } else {
                logger.error("Service provider not exist.");
                throw new UsernameNotFoundException("Service provider not exist.");
            }

        } else {
            logger.error("User data not exist.");
            throw new UsernameNotFoundException("User data not exist.");
        }

        return requestProfile;
    }

}


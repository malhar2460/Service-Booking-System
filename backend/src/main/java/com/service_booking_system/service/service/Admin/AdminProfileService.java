package com.service_booking_system.service.service.Admin;

import com.service_booking_system.service.controller.Admin.RepeatedCode;
import com.service_booking_system.service.dto.Admin.AdminEditProfileRequestDTO;
import com.service_booking_system.service.dto.Admin.AdminProfileResponseDTO;
import com.service_booking_system.service.model.Cities;
import com.service_booking_system.service.model.UserAddress;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.CityRepository;
import com.service_booking_system.service.repository.UserAddressRepository;
import com.service_booking_system.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
public class AdminProfileService {

    @Autowired private RepeatedCode repeatedCode;

    @Autowired private UserRepository userRepository;

    @Autowired private CityRepository cityRepository;

    @Autowired private UserAddressRepository userAddressRepository;

    private static final Logger logger = LoggerFactory.getLogger(AdminProfileService.class);

    // Logic to fetch profile details
    @Transactional
    public AdminProfileResponseDTO getProfileDetail(long userId) throws AccessDeniedException {

        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);

        UserAddress address = userAddressRepository.findByUser(user);

        AdminProfileResponseDTO.AddressDTO addressDTO = null;
        if (address != null) {
            addressDTO = AdminProfileResponseDTO.AddressDTO.builder()
                    .name(address.getName())
                    .areaName(address.getAreaName())
                    .pincode(address.getPincode())
                    .cityName(address.getCity().getCityName())
                    .build();
        }

        return AdminProfileResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhoneNo())
                .email(user.getEmail())
                .addresses(addressDTO)
                .build();
    }

    // Logic to store edited profile details
    @Transactional
    public String editProfile(AdminEditProfileRequestDTO request, long userId) throws Exception {

        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        AdminEditProfileRequestDTO.AddressDTO addr = request.getAddresses();
        if (addr != null) {

            // Validate pincode
            if (addr.getPincode() != null && !addr.getPincode().trim().isEmpty()
                    && !addr.getPincode().matches("^[0-9]{6}$")) {

                logger.error("Pincode must be 6 digits.");
                throw new Exception("Pincode must be 6 digits.");

            }

            // Get city if provided
            Cities city = null;
            if (addr.getCityName() != null && !addr.getCityName().trim().isEmpty()) {
                city = repeatedCode.checkCity(addr.getCityName());
            }

            // Find existing address
            UserAddress existingAddress = userAddressRepository.findByUser(user);

            // Create new address if not exist any
            if (existingAddress == null) {

                UserAddress newAddress = new UserAddress();
                newAddress.setUser(user);
                newAddress.setCity(city);
                newAddress.setName(addr.getName());
                newAddress.setAreaName(addr.getAreaName());
                newAddress.setPincode(addr.getPincode());

                userAddressRepository.save(newAddress);

            } else {

                // Update existing address
                if (city != null) existingAddress.setCity(city);

                if (addr.getName() != null && !addr.getName().trim().isEmpty()) {
                    existingAddress.setName(addr.getName());
                }

                if (addr.getAreaName() != null && !addr.getAreaName().trim().isEmpty()) {
                    existingAddress.setAreaName(addr.getAreaName());
                }

                if (addr.getPincode() != null && !addr.getPincode().trim().isEmpty()) {
                    existingAddress.setPincode(addr.getPincode());
                }

                userAddressRepository.save(existingAddress);
            }
        }

        // Save changes
        userRepository.save(user);

        logger.info("Profile updated successfully.");
        return "Profile updated successfully.";
    }

}


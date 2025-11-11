package com.service_booking_system.service.service.Service_Provider;

import com.service_booking_system.service.Exception.ForbiddenAccessException;
import com.service_booking_system.service.dto.Service_Provider.ServiceProviderRequestDTO;
import com.service_booking_system.service.enums.Status;
import com.service_booking_system.service.enums.UserRoles;
import com.service_booking_system.service.model.Prices;
import com.service_booking_system.service.model.ServiceProvider;
import com.service_booking_system.service.model.SubServices;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.PriceRepository;
import com.service_booking_system.service.repository.ServiceProviderRepository;
import com.service_booking_system.service.repository.SubServiceRepository;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.Customer.CloudinaryService;
import com.service_booking_system.service.service.Customer.EmailService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProviderRequestService {

    @Autowired private UserRepository userRepository;

    @Autowired private ServiceProviderRepository serviceProviderRepository;

    @Autowired private PriceRepository priceRepository;

    @Autowired private EmailService emailService;

    @Autowired private CloudinaryService cloudinaryService;

    @Autowired private SubServiceRepository subServiceRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProviderRequestService.class);

    @Transactional
    public String completeServiceProviderProfile(
            Long userId,
            ServiceProviderRequestDTO data,
            MultipartFile aadharCard,
            MultipartFile panCard,
            MultipartFile utilityBill,
            MultipartFile profilePhoto
    ) throws IOException {
        try {

            // User & Role Validation
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found."));

            if (!UserRoles.SERVICE_PROVIDER.equals(user.getRole())) {
                throw new ForbiddenAccessException("Access denied. Not a service provider.");
            }

            // Check duplicate request
            if (serviceProviderRepository.getByUser(user).isPresent()) {
                return "Your request is already submitted. Please wait for a response.";
            }

            // Upload images
            String folder = "SmartLaundry/ServiceProvider/" + userId;
            String aadharPath = cloudinaryService.uploadFile(aadharCard, folder + "/aadhar");
            String panPath = panCard != null ? cloudinaryService.uploadFile(panCard, folder + "/pan") : null;
            String utilityBillPath = cloudinaryService.uploadFile(utilityBill, folder + "/utility");
            String profilePath = cloudinaryService.uploadFile(profilePhoto, folder + "/profile");

            data.setAadharCardPhoto(aadharPath);
            data.setPanCardPhoto(panPath);
            data.setBusinessUtilityBillPhoto(utilityBillPath);
            data.setProfilePhoto(profilePath);

            // Map and Save ServiceProvider
            ServiceProvider serviceProvider = ServiceProvider.builder()
                    .user(user)
                    .businessName(data.getBusinessName())
                    .businessLicenseNumber(data.getBusinessLicenseNumber())
                    .gstNumber(data.getGstNumber())
                    .aadharCardImage(aadharPath)
                    .panCardImage(panPath)
                    .businessUtilityBillImage(utilityBillPath)
                    .profileImage(data.getProfilePhoto())
                    .bankName(data.getBankName())
                    .ifscCode(data.getIfscCode())
                    .bankAccountNumber(data.getBankAccountNumber())
                    .accountHolderName(data.getAccountHolderName())
                    .status(Status.PENDING)
                    .build();

            serviceProviderRepository.save(serviceProvider);
            logger.info("Service provider saved for user: {}", userId);

            // Save Prices
            if (data.getPriceDTO() != null && !data.getPriceDTO().isEmpty()) {
                List<Prices> prices = data.getPriceDTO().stream().map(priceDto -> {
                    if (priceDto.getSubServiceName() == null || priceDto.getSubServiceName().isEmpty()) {
                        throw new RuntimeException("Sub-service name is missing in price data.");
                    }

                    SubServices subService = subServiceRepository.findBySubServiceName(priceDto.getSubServiceName());

                    if(subService == null){
                        try {
                            throw new NoSuchFieldException("Sub-service not exist.");
                        } catch (NoSuchFieldException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return Prices.builder()
                            .amount(priceDto.getPrice())
                            .subServices(subService)
                            .serviceProvider(serviceProvider)
                            .build();
                }).toList();

                priceRepository.saveAll(prices);
                logger.info("Saved {} price entries for service provider {}", prices.size(), serviceProvider.getServiceProviderId());
            }

            // Send Notification
            String message = "Congratulations! Your request to become a Service Provider has been submitted for review.";
            String subject = "Service Provider Profile Submission";

            emailService.sendMail(user.getEmail(), subject, message);

            logger.info("Notification sent to user {} via SMS and email", userId);

            return "Your request is sent successfully. Wait for a response.";

        } catch (Exception e) {
            logger.error("Error in completing service provider profile for userId {}: {}", userId, e.getMessage(), e);
            throw e;
        }
    }

}


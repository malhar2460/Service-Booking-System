package com.service_booking_system.service.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProviderRequestDTO {
    private Long userId;
    private Long serviceProviderId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private LocalDateTime joinedAt;
    private String businessName;
    private String businessLicenseNumber;
    private String gstNumber;
    private Boolean needOfDeliveryAgent;
    private String profilePhoto;
    private String aadharCardPhoto;
    private String panCardPhoto;
    private String businessUtilityBillPhoto;
    private String bankName;
    private String ifscCode;
    private String bankAccountNumber;
    private String accountHolderName;

    private ServiceProviderRequestDTO.AddressDTO addresses;
    private List<ServiceProviderRequestDTO.priceDTO> priceDTO;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddressDTO {
        private String name;
        private String areaName;
        private String pincode;
        private String cityName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class priceDTO {
        private String serviceName;
        private double amount;
        private String subServiceName;
    }
}


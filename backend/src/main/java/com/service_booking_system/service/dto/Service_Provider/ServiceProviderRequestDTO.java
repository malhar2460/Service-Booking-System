package com.service_booking_system.service.dto.Service_Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceProviderRequestDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String email;
    private String businessName;
    private String businessLicenseNumber;
    private String gstNumber;
    private String profilePhoto;
    private String aadharCardPhoto;
    private String panCardPhoto;
    private String businessUtilityBillPhoto;
    private String bankName;
    private String ifscCode;
    private String bankAccountNumber;
    private String accountHolderName;

    private ServiceProviderRequestDTO.AddressDTO addresses;
    private List<PriceDTO> priceDTO;

    public AddressDTO getAddress() {
        return addresses;
    }

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

}


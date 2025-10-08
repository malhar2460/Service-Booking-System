package com.service_booking_system.service.dto;


import com.service_booking_system.service.enums.UserRoles;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequestDTO {

    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "First name can only contain letters and spaces.")
    private String firstName;

    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Last name can only contain letters and spaces.")
    private String lastName;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits.")
    private String phoneNo;

    @Size(max = 100, message = "Email must not exceed 100 characters.")
    @jakarta.validation.constraints.Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Confirm password is required.")
    private String confirmPassword;

    @NotNull(message = "Role is required.")
    private UserRoles role;

    @Valid
    @NotNull(message = "Address is required.")
    private AddressDTO address;

    @Data
    public static class AddressDTO {
        @NotBlank(message = "Name is required.")
        private String name;

        @NotBlank(message = "Area name is required.")
        private String areaName;

        @NotBlank(message = "Pincode is required.")
        @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits.")
        private String pincode;

        @NotNull(message = "City ID is required.")
        private Long cityId;
    }
}


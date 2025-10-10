
package com.service_booking_system.service.service.Customer;

import com.service_booking_system.service.dto.JwtRequest;
import com.service_booking_system.service.dto.JwtResponse;
import com.service_booking_system.service.dto.RegistrationRequestDTO;
import com.service_booking_system.service.dto.RegistrationResponseDTO;
import com.service_booking_system.service.model.Cities;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.model.UserAddress;
import com.service_booking_system.service.repository.AddressRepository;
import com.service_booking_system.service.repository.CityRepository;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.JWTService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OTPService otpService;
    @Autowired
    private EmailService emailService;

    // Registration
    @Transactional
    public RegistrationResponseDTO registerUser(RegistrationRequestDTO request) {
        if ((request.getEmail() != null && userRepository.findByEmail(request.getEmail()).isPresent()) ||
                userRepository.findByPhoneNo(request.getPhoneNo()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Users user = new Users();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNo(request.getPhoneNo());
        user.setRole(request.getRole());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Users savedUser = userRepository.save(user);

        Cities city = cityRepository.findById(request.getAddress().getCityId())
                .orElseThrow(() -> new RuntimeException("Invalid city ID"));

        UserAddress address = new UserAddress();
        address.setName(request.getAddress().getName());
        address.setAreaName(request.getAddress().getAreaName());
        address.setPincode(request.getAddress().getPincode());
        address.setCity(city);
        address.setUser(savedUser);

        addressRepository.save(address);

        return new RegistrationResponseDTO(savedUser.getPhoneNo(), savedUser.getEmail(), "Successfully Registered");
    }

    // Login
    public ResponseEntity<?> loginUser(JwtRequest request) {
        String input = request.getUsername().trim();
        Optional<Users> optionalUser;

        if (input.matches("^[0-9]{10}$")) {
            optionalUser = userRepository.findByPhoneNo(input);
        } else if (input.contains("@")) {
            optionalUser = userRepository.findByEmail(input.toLowerCase());
        } else {
            throw new BadCredentialsException("Invalid login identifier format");
        }

        Users user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found: " + input));

        // Case 1 → PASSWORD login
        if ("PASSWORD".equalsIgnoreCase(request.getLoginType())) {
            manager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), request.getPassword())
            );

            String token = jwtService.generateToken(user.getUserId(), user.getEmail());
            JwtResponse response = JwtResponse.builder()
                    .jwtToken(token)
                    .username(user.getEmail())
                    .role(user.getRole().toString())
                    .build();
            return ResponseEntity.ok(response);
        }

        // Case 2 → OTP login
        else if ("OTP".equalsIgnoreCase(request.getLoginType())) {
            String otpKey = user.getEmail() != null ? user.getEmail() : user.getPhoneNo();
            String otp = otpService.generateOtp(otpKey);

            if (user.getEmail() != null) {
                emailService.sendOtp(user.getEmail(), otp);
            }
            return ResponseEntity.ok("OTP sent. Please verify.");
        }

        return ResponseEntity.badRequest().body("Invalid login type. Use PASSWORD or OTP.");
    }
}

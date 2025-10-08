//package com.service_booking_system.service.service.Customer;
//
//import com.service_booking_system.service.dto.JwtRequest;
//import com.service_booking_system.service.dto.JwtResponse;
//import com.service_booking_system.service.dto.RegistrationRequestDTO;
//import com.service_booking_system.service.dto.RegistrationResponseDTO;
//import com.service_booking_system.service.model.Cities;
//import com.service_booking_system.service.model.Users;
//import com.service_booking_system.service.model.UserAddress;
//import com.service_booking_system.service.repository.AddressRepository;
//import com.service_booking_system.service.repository.CityRepository;
//import com.service_booking_system.service.repository.UserRepository;
//import com.service_booking_system.service.service.JWTService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import com.service_booking_system.service.Exception.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CityRepository cityRepository;
//
//    @Autowired
//    private JWTService jwtService;
//
//    @Autowired
//    private AuthenticationManager manager;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private  OTPService otpService;
//
//    @Autowired
//    private EmailService emailService;
//
//    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
//
//    //@author Hitiksha Jagani
//    // Logic for registration
//    // Store registration details in database.
//    @Transactional
//    public RegistrationResponseDTO registerUser(RegistrationRequestDTO request) {
//
//        // --- Validate inputs ---
//        if (!request.getFirstName().matches("^[A-Za-z\\s]+$")) throw new FormatException("First name");
//        if (!request.getLastName().matches("^[A-Za-z\\s]+$")) throw new FormatException("Last name");
//        if (!request.getPhoneNo().matches("^[0-9]{10}$")) throw new ExceptionMsg("Phone number must be 10 digits.");
//        if (request.getEmail() != null && request.getEmail().isBlank()) request.setEmail(null);
//
//        if ((request.getEmail() != null && userRepository.findByEmail(request.getEmail()).isPresent()) ||
//                userRepository.findByPhoneNo(request.getPhoneNo()).isPresent()) throw new UserAlreadyExistsException();
//        if (!request.getPassword().equals(request.getConfirmPassword())) throw new PasswordMisMatchException();
//
//        // --- Create user ---
//        Users user = new Users();
//        user.setFirstName(request.getFirstName());
//        user.setLastName(request.getLastName());
//        user.setEmail(request.getEmail());
//        user.setPhoneNo(request.getPhoneNo());
//        user.setRole(request.getRole());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        Users savedUser = userRepository.save(user);
//
//        // --- Create single address ---
//        RegistrationRequestDTO.AddressDTO addr = request.getAddress();
//
//        Cities city = cityRepository.findById(addr.getCityId())
//                .orElseThrow(() -> new RuntimeException("Invalid city ID: " + addr.getCityId()));
//
//        UserAddress address = new UserAddress();
//        address.setName(addr.getName());
//        address.setAreaName(addr.getAreaName());
//        address.setPincode(addr.getPincode());
//        address.setCity(city);
//        address.setUser(savedUser);
//
//        addressRepository.save(address);
//
//        return new RegistrationResponseDTO(savedUser.getPhoneNo(), savedUser.getEmail(), "Successfully Registered");
//    }
//
//
//
////    public String loginUser(JwtRequest request) {
////        String input = request.getUsername().trim();
////        Optional<Users> optionalUser;
////
////        // Phone or Email check
////        if (input.matches("^[0-9]{10}$")) {
////            optionalUser = userRepository.findByPhoneNo(input);
////        } else if (input.contains("@")) {
////            optionalUser = userRepository.findByEmail(input.toLowerCase());
////        } else {
////            throw new BadCredentialsException("Invalid login identifier format");
////        }
////
////        Users user = optionalUser.orElseThrow(
////                () -> new UsernameNotFoundException("User not found: " + input)
////        );
////
////        // ✅ Always authenticate with the canonical username your UserDetailsService expects
////        try {
////            manager.authenticate(
////                    new UsernamePasswordAuthenticationToken(
////                            user.getEmail(), request.getPassword()
////                    )
////            );
////        } catch (BadCredentialsException e) {
////            throw new BadCredentialsException("Invalid username or password!");
////        }
////
////        // Generate OTP
////        String otpKey = user.getEmail() != null ? user.getEmail() : user.getPhoneNo();
////        String otp = otpService.generateOtp(otpKey);
////
////        if (user.getEmail() != null) {
////            emailService.sendOtp(user.getEmail(), otp);
////            return "OTP sent to your email address. Please check and verify.";
////        } else {
////            return "OTP generated. Please verify with your registered phone number.";
////        }
////    }
//public String loginUser(JwtRequest request) {
//    String input = request.getUsername().trim();
//    Optional<Users> optionalUser;
//
//    // Phone or Email check
//    if (input.matches("^[0-9]{10}$")) {
//        optionalUser = userRepository.findByPhoneNo(input);
//    } else if (input.contains("@")) {
//        optionalUser = userRepository.findByEmail(input.toLowerCase());
//    } else {
//        throw new BadCredentialsException("Invalid login identifier format");
//    }
//
//    Users user = optionalUser.orElseThrow(
//            () -> new UsernameNotFoundException("User not found: " + input)
//    );
//
//    // 🔹 Case 1: Password login
//    if ("PASSWORD".equalsIgnoreCase(request.getLoginType())) {
//        try {
//            manager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            user.getEmail(), request.getPassword()
//                    )
//            );
//        } catch (BadCredentialsException e) {
//            throw new BadCredentialsException("Invalid username or password!");
//        }
//        // Generate JWT after successful password login
//        String token = jwtService.generateToken(user.getEmail());
//        return "Login successful. JWT: " + token;
//    }
//
//    // 🔹 Case 2: OTP login
//    else if ("OTP".equalsIgnoreCase(request.getLoginType())) {
//        String otpKey = user.getEmail() != null ? user.getEmail() : user.getPhoneNo();
//        String otp = otpService.generateOtp(otpKey);
//
//        if (user.getEmail() != null) {
//            emailService.sendOtp(user.getEmail(), otp);
//            return "OTP sent to your email address. Please check and verify.";
//        } else {
//            return "OTP generated. Please verify with your registered phone number.";
//        }
//    }
//
//    else {
//        throw new IllegalArgumentException("Invalid login type. Must be PASSWORD or OTP.");
//    }
//}
//
//}
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

            String token = jwtService.generateToken(String.valueOf(user.getUserId()), user.getEmail());
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

//package com.service_booking_system.service.controller.Customer;
//import com.service_booking_system.service.dto.JwtRequest;
//import com.service_booking_system.service.dto.JwtResponse;
//import com.service_booking_system.service.dto.RegistrationRequestDTO;
//import com.service_booking_system.service.dto.RegistrationResponseDTO;
//import com.service_booking_system.service.model.Users;
//import com.service_booking_system.service.repository.UserRepository;
//import com.service_booking_system.service.service.Customer.AuthService;
//import com.service_booking_system.service.service.Customer.EmailService;
//import com.service_booking_system.service.service.Customer.OTPService;
//import com.service_booking_system.service.service.JWTService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.nio.file.AccessDeniedException;
//import java.util.List;
//import java.util.Optional;
//@RestController
//@RequestMapping("")
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private final AuthService authService;
//
//    @Autowired
//    private AuthenticationManager manager;
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    private JWTService jwtService;
//
//    @Autowired
//    private OTPService otpService;
//
//    public AuthController(AuthService authService) {
//        this.authService = authService;
//    }
//
//    // @author hitiksha-jagani
//    // http://localhost:8080/register
//    // Sign up
//    @PostMapping("/register")
//    public ResponseEntity<RegistrationResponseDTO> register(@Valid @RequestBody RegistrationRequestDTO request){
//        return ResponseEntity.ok(authService.registerUser(request));
//    }
//
//    @PostMapping("/login")
//    public String loginWithPassword(@RequestBody JwtRequest request) {
//        return authService.loginUser(request); // sends OTP after verifying credentials
//    }
//
//    // Step 2: Verify OTP and return JWT token
//    @PostMapping("/verify-otp")
//    public ResponseEntity<JwtResponse> verifyOtp(@RequestParam String username, @RequestParam String otp) {
//        String normalizedKey;
//
//        if (username.contains("@")) {
//            normalizedKey = username.toLowerCase();
//        } else {
//            String digitsOnly = username.replaceAll("\\D", "");
//            if (digitsOnly.length() > 10) {
//                digitsOnly = digitsOnly.substring(digitsOnly.length() - 10);
//            }
//            normalizedKey = "+91" + digitsOnly;
//        }
//
//        boolean valid = otpService.validateOtp(normalizedKey, otp);
//        if (!valid) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        Optional<Users> userOpt;
//        if (username.contains("@")) {
//            userOpt = userRepository.findByEmail(username.toLowerCase());
//        } else {
//            userOpt = userRepository.findByPhoneNo(username.replaceAll("\\D", "").substring(0, 10));
//        }
//
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//
//        Users user = userOpt.get();
//        String token = jwtService.generateToken(String.valueOf(user.getUserId()), username);
//        JwtResponse response = JwtResponse.builder()
//                .jwtToken(token)
//                .username(username)
//                .role(user.getRole().toString())
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/resend-otp")
//    public ResponseEntity<String> resendOtp(@RequestParam String username) {
//        String normalizedKey;
//        Optional<Users> userOpt;
//
//        // Normalize username
//        if (username.contains("@")) {
//            normalizedKey = username.toLowerCase();
//            userOpt = userRepository.findByEmail(normalizedKey);
//        } else {
//            String digitsOnly = username.replaceAll("\\D", "");
//            if (digitsOnly.length() > 10) {
//                digitsOnly = digitsOnly.substring(digitsOnly.length() - 10);
//            }
//            normalizedKey = "+91" + digitsOnly;
//            userOpt = userRepository.findByPhoneNo(digitsOnly);
//        }
//
//        if (userOpt.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//        }
//
//        // Check cooldown before resending
//        if (otpService.isInCooldown(normalizedKey)) {
//            long remaining = otpService.remainingCooldownMillis(normalizedKey) / 1000;
//            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
//                    .body("Please wait " + remaining + " seconds before requesting another OTP.");
//        }
//
//        Users user = userOpt.get();
//
//        // Generate new OTP
//        String otp = otpService.generateOtp(normalizedKey);
//
//        // Send OTP
//            emailService.sendOtp(user.getEmail(), otp);
//            return ResponseEntity.ok("OTP resent to your email.");
//
//    }
//
//
//}


package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.dto.Customer.OtpVerificationRequest;
import com.service_booking_system.service.dto.JwtRequest;
import com.service_booking_system.service.dto.JwtResponse;
import com.service_booking_system.service.dto.RegistrationRequestDTO;
import com.service_booking_system.service.dto.RegistrationResponseDTO;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.Customer.AuthService;
import com.service_booking_system.service.service.Customer.EmailService;
import com.service_booking_system.service.service.Customer.OTPService;
import com.service_booking_system.service.service.JWTService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JWTService jwtService;

    //  Registration
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDTO> register(@Valid @RequestBody RegistrationRequestDTO request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    //  Login (Password OR OTP)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        return authService.loginUser(request);
    }

    //  Verify OTP → Issue JWT
    @PostMapping("/verify-otp")
    public ResponseEntity<JwtResponse> verifyOtp(@RequestBody OtpVerificationRequest request) {
        String username = request.getUsername();
        String otp = request.getOtp();

        Optional<Users> userOpt = username.contains("@")
                ? userRepository.findByEmail(username.toLowerCase())
                : userRepository.findByPhoneNo(username.replaceAll("\\D", "").substring(0, 10));

        Users user = userOpt.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String otpKey = user.getEmail() != null ? user.getEmail() : user.getPhoneNo();

        if (!otpService.validateOtp(otpKey, otp)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = jwtService.generateToken(String.valueOf(user.getUserId()), user.getEmail());
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(username)
                .role(user.getRole().toString())
                .build();

        return ResponseEntity.ok(response);
    }


    //  Resend OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String username) {
        Optional<Users> userOpt = username.contains("@")
                ? userRepository.findByEmail(username.toLowerCase())
                : userRepository.findByPhoneNo(username.replaceAll("\\D", "").substring(0, 10));

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        Users user = userOpt.get();
        String otpKey = user.getEmail() != null ? user.getEmail() : user.getPhoneNo();
        String otp = otpService.generateOtp(otpKey);

        if (user.getEmail() != null) {
            emailService.sendOtp(user.getEmail(), otp);
        }
        return ResponseEntity.ok("OTP resent.");
    }
}

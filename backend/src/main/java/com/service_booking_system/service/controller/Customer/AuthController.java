package com.service_booking_system.service.controller.Customer;

import com.service_booking_system.service.dto.*;
import com.service_booking_system.service.dto.Customer.OtpVerificationRequest;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import com.service_booking_system.service.service.ChangePasswordService;
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
import org.springframework.security.core.Authentication;

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
    @Autowired
    private ChangePasswordService changePasswordService;
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


    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO
    ) throws Exception {
        // Get logged-in user details (from JWT)
        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));
        String response = changePasswordService.changePassword(user.getUserId(), changePasswordRequestDTO);
        return ResponseEntity.ok(response);
    }
}

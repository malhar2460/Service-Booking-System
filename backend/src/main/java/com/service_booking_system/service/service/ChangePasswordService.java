package com.service_booking_system.service.service;

import com.service_booking_system.service.controller.Admin.RepeatedCode;
import com.service_booking_system.service.dto.ChangePasswordRequestDTO;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangePasswordService {

    @Autowired private UserRepository userRepository;

    @Autowired private RepeatedCode repeatedCode;

    @Autowired private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(ChangePasswordService.class);

    public String changePassword(long userId, ChangePasswordRequestDTO changePasswordRequestDTO) throws Exception {

        Users user = repeatedCode.checkUser(userId);

        // Check whether old password match with stored password or not.
        if (!passwordEncoder.matches(changePasswordRequestDTO.getOldPassword(), user.getPassword())) {
            logger.error("Old password does not match.");
            throw new IllegalArgumentException("Old password does not match.");
        }

        // Check if new password not match with old password
        if (changePasswordRequestDTO.getOldPassword().equals(changePasswordRequestDTO.getNewPassword())) {
            logger.error("New password must be different from old password.");
            throw new IllegalArgumentException("New password must be different from old password.");
        }

        // Check password and confirm password
        if(!changePasswordRequestDTO.getNewPassword().equals(changePasswordRequestDTO.getConfirmPassword())){
            logger.error("Password and confirm password not match.");
            throw new Exception("Password and confirm password not match.");
        }

        // Password security check (min 8 chars, at least one upper, one lower, one number, one special char)
        String newPassword = changePasswordRequestDTO.getNewPassword();
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

        if (!newPassword.matches(passwordPattern)) {
            logger.error("Password does not meet security requirements.");
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters long, contain at least one uppercase letter, "
                            + "one lowercase letter, one number, and one special character."
            );
        }

        // Save encoded password
        user.setPassword(passwordEncoder.encode(changePasswordRequestDTO.getNewPassword()));
        userRepository.save(user);

        logger.info("Password changed successfully.");
        return "Password changed successfully.";
    }

}


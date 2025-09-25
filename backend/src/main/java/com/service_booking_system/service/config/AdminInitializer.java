// /backend/src/main/java/com/service_booking_system/service/config/AdminInitializer.java

package com.service_booking_system.service.config;

import com.service_booking_system.service.enums.UserRoles;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdminUser() {
        boolean adminExists = userRepository.existsByRole(UserRoles.ADMIN);

        if (!adminExists) {
            Users admin = new Users();
            admin.setFirstName("Dummy");
            admin.setLastName("Admin");
            admin.setEmail("admin@example.com");
            admin.setPhoneNo("9999999999");
            admin.setRole(UserRoles.ADMIN);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setCreatedAt(LocalDateTime.now());
            admin.setBlocked(false);

            userRepository.save(admin);

            System.out.println("Default admin created: admin@example.com / admin123");
        } else {
            System.out.println("Admin already exists. Skipping creation.");
        }
    }

}


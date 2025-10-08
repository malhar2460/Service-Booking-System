// /backend/src/main/java/com/service_booking_system/service/model/Users.java

package com.service_booking_system.service.model;

import com.service_booking_system.service.enums.UserRoles;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "First name contains invalid characters.")
    @Column(name = "first_name", nullable = false, unique = false, length = 100)
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Last name contains invalid characters.")
    @Column(name = "last_name", nullable = false, unique = false, length = 100)
    private String lastName;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact phone must be a 10-12 digit number, optionally starting with '+'.")
    @Column(name = "phone_no", nullable = false, unique = true, length = 15)
    private String phoneNo;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @NotBlank(message = "Password is required.")
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRoles role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_blocked")
    private boolean isBlocked = false;
}




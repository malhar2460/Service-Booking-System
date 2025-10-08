// /backend/src/main/java/com/service_booking_system/service/model/Services.java

package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "services", uniqueConstraints = {
        @UniqueConstraint(columnNames = "service_name", name = "uk_service_name")
})
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_id", updatable = false, nullable = false)
    private Long serviceId;

    @NotBlank(message = "Service name is required.")
    @Size(min = 3, max = 100, message = "Service name must be between 3 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z+\\s\\-_()]+$", message = "Service name contains invalid characters.")
    @Column(name = "service_name", nullable = false, unique = true, length = 100)
    private String serviceName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;


    @JsonManagedReference
    @OneToMany(mappedBy = "services", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubServices> subServices = new ArrayList<>();

    @Column(name = "ServicePhoto", nullable = true)
    private String serviceImage;
}


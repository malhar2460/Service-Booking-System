// /backend/src/main/java/com/service_booking_system/service/model/SubServices.java

package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "sub_service", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "service_id", "sub_service_name" }, name = "uk_sub_service_name")
})
public class SubServices {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sub_service_id", updatable = false, nullable = false)
    private String subServiceId;

    @NotBlank(message = "Sub service name is required.")
    @Size(min = 3, max = 100, message = "Sub service name must be between 3 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z+()\\s-]+$", message = "Service name contains invalid characters.")
    @Column(name = "sub_service_name", nullable = false,  length = 100)
    private String subServiceName;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "service_id", nullable = false)
    private Services services;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

}



// /backend/src/main/java/com/service_booking_system/service/model/RevenueBreakDown.java

package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service_booking_system.service.enums.CurrentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "revenue_breakdown")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueBreakDown {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "revenue_id")
    private Long revenueId;

    @Column(name = "service_provider")
    private Double serviceProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CurrentStatus currentStatus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "active_at")
    private LocalDateTime activeAt;

    @Column(name = "deactivate_at")
    private LocalDateTime deactivateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private Users user;
}


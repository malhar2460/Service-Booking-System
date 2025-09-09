package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_revenue")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRevenue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "admin_revenue_id", nullable = false)
    private Long adminRevenueId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "profit")
    private Double profit;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}


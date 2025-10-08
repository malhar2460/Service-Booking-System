// /backend/src/main/java/com/service_booking_system/service/model/Payout.java

package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service_booking_system.service.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "payout")
public class Payout {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "payout_id", nullable = false, updatable = false)
    private Long payoutId;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "service_earning")
    private Double serviceEarning;

    @Column(name = "charge")
    private Double charge;

    @NotNull(message = "Amount is required.")
    @DecimalMin(value = "0.0", inclusive = false)
    @Column(name = "final_amount", nullable = false)
    private Double finalAmount;

    @Column(name = "transaction_id")
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "service_provider_id", nullable = false)
    private ServiceProvider serviceProvider;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}



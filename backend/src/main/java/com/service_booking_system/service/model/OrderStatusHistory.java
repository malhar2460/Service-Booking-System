// /backend/src/main/java/com/service_booking_system/service/model/OrderStatusHistory.java

package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service_booking_system.service.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_status_history")
@ToString(onlyExplicitlyIncluded = true)
public class OrderStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference
    private Orders order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonIgnore
    private OrderStatus status;

    @CreationTimestamp
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

}


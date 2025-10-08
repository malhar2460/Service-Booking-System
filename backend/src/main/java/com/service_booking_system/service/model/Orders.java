// /backend/src/main/java/com/service_booking_system/service/model/Orders.java

package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.service_booking_system.service.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
@ToString(exclude = {"statusHistory", "orderOtpList", "serviceProvider"})
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_id", updatable = false, nullable = false)
    private Long orderId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "service_provider_id", foreignKey = @ForeignKey(name = "fk_order_service_provider"), nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ServiceProvider serviceProvider;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @NotNull(message = "Date is required.")
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull(message = "Time is required.")
    @Column(name = "time", nullable = false)
    private LocalTime time;

    @NotBlank(message = "Contact name is required.")
    @Size(max = 100, message = "Contact name must not exceed 100 characters.")
    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;

    @NotBlank(message = "Contact phone is required.")
    @Pattern(regexp = "^\\+?[0-9]{10}$", message = "Contact phone must be a 10-12 digit number, optionally starting with '+'.")
    @Column(name = "contact_phone", nullable = false, length = 15)
    private String contactPhone;

    @NotBlank(message = "Contact address is required.")
    @Size(max = 255, message = "Contact address must not exceed 255 characters.")
    @Column(name = "contact_address", nullable = false)
    private String contactAddress;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "price_id", nullable = false)
    private Prices price;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @JsonBackReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistory> statusHistory = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderOtp> orderOtpList;
}

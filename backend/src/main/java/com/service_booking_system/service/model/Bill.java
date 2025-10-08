// /backend/src/main/java/com/service_booking_system/service/model/Bill.java

package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.service_booking_system.service.enums.BillStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bill")
public class Bill implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "invoice_number", nullable = false, updatable = false)
    private Long invoiceNumber;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Orders order;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    private BillStatus status = BillStatus.PENDING;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "charge", nullable = false)
    private Double charge;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "gst", nullable = false)
    private Double gst;

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(name = "total", nullable = false)
    private Double total;

}


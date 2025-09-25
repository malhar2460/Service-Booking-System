// /backend/src/main/java/com/service_booking_system/service/model/ServiceProvider.java

package com.service_booking_system.service.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_provider")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "service_provider_id", nullable = false, updatable = false)
    private Long serviceProviderId;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_license_number", nullable = false, unique = true)
    private String businessLicenseNumber;

    @Column(name = "gst_number", nullable = false, unique = true)
    private String gstNumber;

    @Column(name = "profile_image", nullable = false)
    private String profileImage;

    @Column(name = "pan_card_image", nullable = true)
    private String panCardImage;

    @Column(name = "aadha_card_image", nullable = false)
    private String aadharCardImage;

    @Column(name = "business_utility_bill_image", nullable = false)
    private String businessUtilityBillImage;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "bank_account_number", nullable = false, unique = true)
    private String bankAccountNumber;

    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;

    @Column(name = "ifsc_code", nullable = false)
    private String ifscCode;

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude
    private List<Prices> prices = new ArrayList<>();

    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users user;

}


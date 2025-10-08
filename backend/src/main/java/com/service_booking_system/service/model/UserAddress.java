// /backend/src/main/java/com/service_booking_system/service/model/UserAddress.java

package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_address")
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "add_id")
    private Long addressId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @NotBlank(message = "Name is required.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Area name is required.")
    @Column(name = "area_name", nullable = false,  length = 100)
    private String areaName;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false)
    private Cities city;

    @NotBlank(message = "Pincode is required.")
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits")
    @Column(name = "pincode", nullable = false, length = 6)
    private String pincode;

}



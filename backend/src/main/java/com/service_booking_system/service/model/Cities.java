// /backend/src/main/java/com/service_booking_system/service/model/Cities.java

package com.service_booking_system.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cities", uniqueConstraints = {
        @UniqueConstraint(columnNames = "city_name", name = "uk_city_name")
})
public class Cities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "city_id", updatable = false, nullable = false)
    private Long cityId;

    @NotBlank(message = "City name is required.")
    @Size(max = 100, message = "City name must be less than 100 characters.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "City name contains invalid characters.")
    @Column(name = "city_name", unique = true, nullable = false, length = 100)
    private String cityName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private States states;

}



// /backend/src/main/java/com/service_booking_system/service/model/States.java

package com.service_booking_system.service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "states", uniqueConstraints = {
        @UniqueConstraint(columnNames = "state_name", name = "uk_state_name")
})
public class States {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "state_id", updatable = false, nullable = false)
    private Long stateId;

    @NotBlank(message = "State name is required.")
    @Size(max = 100, message = "State name must be less than 100 characters.")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "State name contains invalid characters.")
    @Column(name = "state_name", unique = true, nullable = false, length = 100)
    private String stateName;

}




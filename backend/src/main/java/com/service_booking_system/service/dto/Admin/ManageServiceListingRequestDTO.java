// /backend/src/main/java/com/service_booking_system/service/dto/Admin/ManageServiceListingRequestDTO.java

package com.service_booking_system.service.dto.Admin;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManageServiceListingRequestDTO {
    @Size(min = 3, max = 100, message = "Service name must be between 3 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z+\\s\\-_()]+$", message = "Service name contains invalid characters.")
    private String serviceName;

    @Size(min = 3, max = 100, message = "Sub Service name must be between 3 and 100 characters.")
    @Pattern(regexp = "^[A-Za-z+\\s\\-_()]+$", message = "Sub Service name contains invalid characters.")
    private String subServiceName;
}

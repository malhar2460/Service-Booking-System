//  /backend/src/main/java/com/service_booking_system/service/dto/Admin/ServiceSummaryDTO.java

package com.service_booking_system.service.dto.Admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceSummaryDTO {
    private Long serviceId;
    private String serviceName;
    private Long count;
}

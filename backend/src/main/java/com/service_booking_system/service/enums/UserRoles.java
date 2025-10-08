// /backend/src/main/java/com/service_booking_system/service/enums/UserRoles.java

package com.service_booking_system.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRoles {
    ADMIN,
    SERVICE_PROVIDER,
    CUSTOMER;

    @JsonCreator
    public static UserRoles fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Role is required.");
        }
        try {
            return UserRoles.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role: " + value + ". Allowed values are: CUSTOMER, ADMIN, SERVICE_PROVIDER.");
        }
    }

    @JsonValue
    public String toValue(){
        return this.name();
    }
}

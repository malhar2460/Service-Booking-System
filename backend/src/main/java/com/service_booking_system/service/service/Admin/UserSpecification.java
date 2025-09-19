package com.service_booking_system.service.service.Admin;

import com.service_booking_system.service.model.Users;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {

    public static Specification<Users> searchByEmailOrPhone(String keyword) {
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("phoneNo")), "%" + keyword.toLowerCase() + "%")
        );
    }

    public static Specification<Users> joinDateBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) ->
                cb.between(root.get("createdAt"), startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());
    }

}
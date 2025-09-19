package com.service_booking_system.service.repository;

import com.service_booking_system.service.enums.UserRoles;
import com.service_booking_system.service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {

    boolean existsByRole(UserRoles userRoles);

    long countByRoleAndCreatedAtBetween(UserRoles role, LocalDateTime start, LocalDateTime end);

    @Query("""
        SELECT ua.city.cityName, COUNT(u.userId)
        FROM Users u
        JOIN UserAddress ua ON u.userId = ua.user.userId
        WHERE u.role = :role
        GROUP BY ua.city.cityName
    """)
    List<Object[]> countCustomersGroupedByCity(UserRoles role);

}

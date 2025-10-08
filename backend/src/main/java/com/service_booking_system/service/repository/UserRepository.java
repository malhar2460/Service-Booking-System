// /backend/src/main/java/com/service_booking_system/service/repository/UserRepository.java

package com.service_booking_system.service.repository;


//import com.service_booking_system.service.model.UserPrincipal;
import com.service_booking_system.service.enums.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import com.service_booking_system.service.model.Users;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
@Repository


public interface UserRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhoneNo(String phone);

    List<Users> findAllByOrderByUserIdAsc();


    long countByRoleAndCreatedAtBetween(UserRoles role, LocalDateTime start, LocalDateTime end);

    boolean existsByRole(UserRoles admin);
    @Query("""
        SELECT ua.city.cityName, COUNT(u.userId)
        FROM Users u
        JOIN UserAddress ua ON u.userId = ua.user.userId
        WHERE u.role = :role
        GROUP BY ua.city.cityName
    """)
    List<Object[]> countCustomersGroupedByCity(UserRoles role);

}

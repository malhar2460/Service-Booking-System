package com.service_booking_system.service.repository;

import com.service_booking_system.service.enums.UserRoles;
import com.service_booking_system.service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {

    boolean existsByRole(UserRoles userRoles);
}

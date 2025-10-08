// /backend/src/main/java/com/service_booking_system/service/repository/UserAddressRepository.java

package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.UserAddress;
import com.service_booking_system.service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    UserAddress findByUser(Users user);
}

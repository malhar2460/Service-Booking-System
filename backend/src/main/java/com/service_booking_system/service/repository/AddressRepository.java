package com.service_booking_system.service.repository;

import com.service_booking_system.service.model.UserAddress;
import com.service_booking_system.service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<UserAddress, Long> {
    Optional<UserAddress> findByUser(Users user);

}

package com.service_booking_system.service.service.Customer;


import com.service_booking_system.service.dto.Customer.UserUpdateDto;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerProfileService {

    private final UserRepository usersRepository;

    public Users updateUserProfile(UserUpdateDto dto) {
        Users existingUser = usersRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existingUser.setFirstName(dto.getFirstName());
        existingUser.setLastName(dto.getLastName());
        existingUser.setPhoneNo(dto.getPhoneNo());
        existingUser.setEmail(dto.getEmail());

        return usersRepository.save(existingUser);
    }

}

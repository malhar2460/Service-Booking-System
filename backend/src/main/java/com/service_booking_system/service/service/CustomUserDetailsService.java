package com.service_booking_system.service.service;


import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.model.UserPrincipal;
import com.service_booking_system.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

//@author Hitiksha Jagani
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> userOptional;

        if (username.matches("^[0-9]{10}$")) {
            userOptional = userRepository.findByPhoneNo(username);
        } else {
            userOptional = userRepository.findByEmail(username);
        }

        Users users = userOptional.orElseThrow(
                () -> new UsernameNotFoundException("User not found with: " + username)
        );

        return new UserPrincipal(users, username);
    }
}

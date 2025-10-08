// /backend/src/main/java/com/service_booking_system/service/controller/Admin/RepeatedCode.java

package com.service_booking_system.service.controller.Admin;

import com.service_booking_system.service.enums.UserRoles;
import com.service_booking_system.service.model.Cities;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.CityRepository;
import com.service_booking_system.service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
public class RepeatedCode {

//    @Autowired private JWTService jwtService;

    @Autowired private UserRepository userRepository;

    @Autowired private CityRepository cityRepository;

    private static final Logger logger = LoggerFactory.getLogger(RepeatedCode.class);

    public long fetchUserIdFromToken(HttpServletRequest request) {
//        return (String) jwtService.extractUserId(jwtService.extractTokenFromHeader(request));
        return 1;
    }

    // Check user exist or not and if exist then return user
    public Users checkUser(long userId){

        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not exist."));
    }

    // Check city exist or not and if exist then return city
    public Cities checkCity(String cityName){

        return cityRepository.findByCityName(cityName)
                .orElseThrow(() -> new RuntimeException("Invalid city: " + cityName));
    }

    // Check whether user is admin or not
    public void isAdmin(Users user) throws AccessDeniedException {
        if (!UserRoles.ADMIN.equals(user.getRole())) {
            logger.error("You are not applicable for this page.");
            throw new AccessDeniedException("You are not applicable for this page.");
        }
    }

}


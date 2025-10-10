// /backend/src/main/java/com/service_booking_system/service/controller/Admin/SettingController.java

package com.service_booking_system.service.controller.Admin;

import com.service_booking_system.service.dto.Admin.RevenueSettingRequestDTO;
import com.service_booking_system.service.dto.Admin.RevenueSettingResponseDTO;
import com.service_booking_system.service.dto.CityDTO;
import com.service_booking_system.service.dto.StateDTO;
import com.service_booking_system.service.model.RevenueBreakDown;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.service.Admin.SettingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/configurations")
public class SettingController {

    @Autowired private SettingService settingService;

    @Autowired private RepeatedCode repeatedCode;

    // http://localhost:8080/configurations/revenue-breakdown
    // Set revenue breakdown
    @PostMapping("/revenue-breakdown")
    public ResponseEntity<RevenueSettingResponseDTO> setRevenue(HttpServletRequest request, @RequestBody @Valid RevenueSettingRequestDTO revenueSettingRequestDTO) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(settingService.setRevenue(revenueSettingRequestDTO, user));
    }

    // http://localhost:8080/configurations/revenue-breakdown/history
    // Get revenue breakdown
    @GetMapping("/revenue-breakdown/history")
    public ResponseEntity<List<RevenueBreakDown>> getRevenue(HttpServletRequest request) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(settingService.getRevenue());
    }

    // http://localhost:8080/configurations/revenue-breakdown/history/{id}
    // Toggle status of revenue breakdown. If current is ACTIVE then set to INACTIVE and vice versa.
    @PutMapping("/revenue-breakdown/history/{id}")
    public ResponseEntity<String> changeRevenueBreakdownStatus(HttpServletRequest request, @PathVariable Long id) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(settingService.changeRevenueBreakdownStatus(id));
    }

    // http://localhost:8080/configurations/add-state
    // Add new state data
    @PostMapping("/add-state")
    public ResponseEntity<String> addState(HttpServletRequest request, @RequestBody @Valid StateDTO stateDTO) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(settingService.addState(stateDTO));
    }

    // http://localhost:8080/configurations/add-city
    // Add new state data
    @PostMapping("/add-city")
    public ResponseEntity<String> addCity(HttpServletRequest request, @RequestBody @Valid CityDTO cityDTO) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(settingService.addCity(cityDTO));
    }
}

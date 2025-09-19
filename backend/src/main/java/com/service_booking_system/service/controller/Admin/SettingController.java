package com.service_booking_system.service.controller.Admin;

import com.service_booking_system.service.dto.Admin.RevenueSettingRequestDTO;
import com.service_booking_system.service.dto.Admin.RevenueSettingResponseDTO;
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
    // Change status of revenue breakdown
    @PutMapping("/revenue-breakdown/history/{id}")
    public ResponseEntity<String> changeRevenueBreakdownStatus(HttpServletRequest request, @PathVariable Long id) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        Users user = repeatedCode.checkUser(userId);
        repeatedCode.isAdmin(user);
        return ResponseEntity.ok(settingService.changeRevenueBreakdownStatus(id));
    }

}

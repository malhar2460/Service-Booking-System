// /backend/src/main/java/com/service_booking_system/service/controller/Admin/AdminProfileController.java

package com.service_booking_system.service.controller.Admin;

import com.service_booking_system.service.dto.Admin.AdminEditProfileRequestDTO;
import com.service_booking_system.service.dto.Admin.AdminProfileResponseDTO;
import com.service_booking_system.service.dto.ChangePasswordRequestDTO;
import com.service_booking_system.service.service.Admin.AdminProfileService;
import com.service_booking_system.service.service.ChangePasswordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/profile")
public class AdminProfileController {

    @Autowired private AdminProfileService adminProfileService;

    @Autowired private RepeatedCode repeatedCode;

    @Autowired private ChangePasswordService changePasswordService;

    // http://localhost:8080/profile/view
    // Return profile detail of the admin.
    @GetMapping("/view")
    public ResponseEntity<AdminProfileResponseDTO> getAdminDetail(HttpServletRequest request) throws AccessDeniedException {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(adminProfileService.getProfileDetail(userId));
    }

    // http://localhost:8080/profile/edit
    // Edit admin existing profile details.
    @PutMapping("/edit")
    public ResponseEntity<String> editProfile(@RequestBody AdminEditProfileRequestDTO adminEditProfileRequestDTO,
                                              HttpServletRequest request) throws Exception {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(adminProfileService.editProfile(adminEditProfileRequestDTO, userId));
    }

    // http://localhost:8080/profile/change-password
    // Change password
    @PutMapping("/change-password")
    public ResponseEntity<String> changeAdminPassword(HttpServletRequest request, @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) throws Exception {
        long userId = repeatedCode.fetchUserIdFromToken(request);
        return ResponseEntity.ok(changePasswordService.changePassword(userId, changePasswordRequestDTO));
    }

}


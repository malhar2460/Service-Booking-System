// /backend/src/main/java/com/service_booking_system/service/controller/Admin/ImageController.java

package com.service_booking_system.service.controller.Admin;

import com.service_booking_system.service.model.ServiceProvider;
import com.service_booking_system.service.model.Users;
import com.service_booking_system.service.repository.ServiceProviderRepository;
import com.service_booking_system.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired private UserRepository userRepository;

    @Autowired private ServiceProviderRepository serviceProviderRepository;

    @Autowired private RepeatedCode repeatedCode;

    // http://localhost:8080/image/provider/{type}/{userId}
    @GetMapping("/provider/{type}/{userId}")
    public ResponseEntity<byte[]> fetchProviderImage(
            @PathVariable String type,
            @PathVariable Long userId) throws IOException {

        Users user = repeatedCode.checkUser(userId);

        ServiceProvider serviceProvider = serviceProviderRepository.findByUser(user);

        String imageData;
        switch (type.toLowerCase()) {
            case "profile": imageData = serviceProvider.getProfileImage(); break;
            case "aadhar": imageData = serviceProvider.getAadharCardImage(); break;
            case "pan": imageData = serviceProvider.getPanCardImage(); break;
            case "utilitybill": imageData = serviceProvider.getBusinessUtilityBillImage(); break;
            default: return ResponseEntity.badRequest().build();
        }

        File imageFile = new File(imageData);
        if (!imageFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        byte[] imagePath  = Files.readAllBytes(imageFile.toPath());

        // Detect file type
        String mimeType = Files.probeContentType(imageFile.toPath());
        MediaType mediaType = mimeType != null ? MediaType.parseMediaType(mimeType) : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(imagePath);
    }

}


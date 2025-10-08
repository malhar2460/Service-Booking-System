package com.service_booking_system.service.service.Customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OTPService {

    private final SecureRandom random = new SecureRandom();

    private static final Duration OTP_TTL = Duration.ofMinutes(5);
    private static final Duration COOLDOWN_DURATION = Duration.ofSeconds(60); // 60-second cooldown

    // In-memory storage
    private final Map<String, OtpEntry> otpStorage = new ConcurrentHashMap<>();

    private static class OtpEntry {
        String otp;
        long timestamp; // when OTP was generated

        OtpEntry(String otp, long timestamp) {
            this.otp = otp;
            this.timestamp = timestamp;
        }
    }

    private String generateKey(String identifier) {
        return identifier.toLowerCase().trim();
    }

    public boolean isInCooldown(String identifier) {
        String key = generateKey(identifier);
        OtpEntry entry = otpStorage.get(key);
        if (entry != null) {
            long now = System.currentTimeMillis();
            return (now - entry.timestamp) < COOLDOWN_DURATION.toMillis();
        }
        return false;
    }

    public long remainingCooldownMillis(String identifier) {
        String key = generateKey(identifier);
        OtpEntry entry = otpStorage.get(key);
        if (entry != null) {
            long now = System.currentTimeMillis();
            long diff = now - entry.timestamp;
            return Math.max(0, COOLDOWN_DURATION.toMillis() - diff);
        }
        return 0;
    }

    public String generateOtp(String identifier) {
        String key = generateKey(identifier);
        String otp = String.format("%06d", random.nextInt(1000000));
        otpStorage.put(key, new OtpEntry(otp, System.currentTimeMillis()));

        // Clean up expired OTPs asynchronously (optional)
        otpStorage.entrySet().removeIf(e -> System.currentTimeMillis() - e.getValue().timestamp > OTP_TTL.toMillis());

        return otp;
    }

    public boolean validateOtp(String identifier, String otp) {
        String key = generateKey(identifier);
        OtpEntry entry = otpStorage.get(key);

        if (entry != null && entry.otp.equals(otp)) {
            otpStorage.remove(key); // remove OTP after successful validation
            return true;
        }
        return false;
    }
}

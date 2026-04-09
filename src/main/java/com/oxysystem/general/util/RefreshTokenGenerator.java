package com.oxysystem.general.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

public class RefreshTokenGenerator {

    public static String generate() {
        return UUID.randomUUID().toString() + UUID.randomUUID();
    }

    public static String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : encoded) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash refresh token", e);
        }
    }
}

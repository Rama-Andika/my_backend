package com.oxysystem.general.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.security.SecureRandom;

public class GenerateRandomIdUtils implements IdentifierGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int RANDOM_DIGITS = 4; // 4 digit random, ubah sesuai kebutuhan

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        // Ambil current timestamp (milidetik)
        long timestamp = System.currentTimeMillis();

        // Generate angka random 4 digit (0000 - 9999)
        int randomNumber = secureRandom.nextInt((int) Math.pow(10, RANDOM_DIGITS));

        // Format angka random agar selalu 4 digit (contoh: 0073)
        String formattedRandom = String.format("%0" + RANDOM_DIGITS + "d", randomNumber);

        // Gabungkan timestamp dan angka random
        String finalId = timestamp + formattedRandom;

        return Long.parseLong(finalId); // Jika pakai Long sebagai ID
    }
}

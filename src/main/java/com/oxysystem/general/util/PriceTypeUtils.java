package com.oxysystem.general.util;

import com.oxysystem.general.model.db1.posmaster.PriceType;

import java.lang.reflect.Field;

public class PriceTypeUtils {
    /**
     * Mendapatkan nilai harga golongan dari PriceType berdasarkan string "gol_1", "gol_2", dst.
     * @param priceType instance dari entity PriceType
     * @param golonganField nama string seperti "gol_1", "gol_10", dst.
     * @return BigDecimal nilai harga, atau null jika tidak ditemukan
     */
    public static Double getPriceByGol(PriceType priceType, String golonganField) {
        if (priceType == null || golonganField == null || !golonganField.startsWith("gol_")) {
            return 0.0;
        }

        String fieldName = golonganField.replace("gol_", "gol"); // convert "gol_1" -> "gol1"

        try {
            Field field = PriceType.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(priceType);
            return value != null ? (Double) value : 0.0;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("failed to get price from: " + fieldName);
            return 0.0;
        }
    }
}

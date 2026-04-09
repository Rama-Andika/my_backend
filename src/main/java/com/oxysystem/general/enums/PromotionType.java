package com.oxysystem.general.enums;

public enum PromotionType {
    DISCOUNT(0),
    BUNDLING(1),
    PWP(2),
    CATEGORY(5),
    CLAIM(6),
    TEBUS_MURAH(7),
    CONSIGMENT(8),
    OVERALL_DISCOUNT(9),
    TIERED(10);

    private int value;

    PromotionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

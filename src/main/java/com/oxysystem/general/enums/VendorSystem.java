package com.oxysystem.general.enums;

public enum VendorSystem {
    HARGA_JUAL(1),
    HARGA_BELI(2);

    private int value;

    VendorSystem(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

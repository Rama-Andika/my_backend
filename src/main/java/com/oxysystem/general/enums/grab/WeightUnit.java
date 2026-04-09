package com.oxysystem.general.enums.grab;

import com.fasterxml.jackson.annotation.JsonValue;

public enum WeightUnit {
    G("g"),
    KG("lg"),
    ml("ml"),
    L("L"),
    PER_PACK("per pack");

    private final String displayName;

    WeightUnit(String displayName){
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}

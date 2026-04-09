package com.oxysystem.general.enums.grab;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CampaignCreateBy {
    GRAB("Grab"),
    MERCHANT("Merchant"),
    PARTNER("Partner");

    private final String displayName;

    CampaignCreateBy(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}

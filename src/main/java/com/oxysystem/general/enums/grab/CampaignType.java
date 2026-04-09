package com.oxysystem.general.enums.grab;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CampaignType {
    NET("net"),
    PERCENTAGE("percentage"),
    DELIVERY("delivery"),
    FREE_ITEM("freeItem"),
    BUNDLE_SAME_NET("bundleSameNet"),
    BUNDLE_SAME_PERCENTAGE("bundleSamePercentage"),
    BUNDLE_SAME_FIX_PRICE("bundleSameFixPrice"),
    BUNDLE_DIFF_NET("bundleDiffNet"),
    BUNDLE_DIFF_PERCENTAGE("bundleDiffPercentage"),
    BUNDLE_DIFF_FIX_PRICE("bundleDiffFixPrice");

    private final String displayName;

    CampaignType(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static CampaignType fromDisplayName(String displayName) {
        for (CampaignType type : CampaignType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid GrabDiscountType display name: " + displayName);
    }

    public static String getKeyByDisplayName(String displayName) {
        return fromDisplayName(displayName).name();
    }
}

package com.oxysystem.general.response.grab;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrabMartAccessTokenFailedResponse {
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    public GrabMartAccessTokenFailedResponse(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }
}

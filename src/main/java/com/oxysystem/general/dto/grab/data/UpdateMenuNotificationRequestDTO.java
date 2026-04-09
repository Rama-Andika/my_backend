package com.oxysystem.general.dto.grab.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateMenuNotificationRequestDTO {
    @NotNull(message = "merchant ID cannot be empty!")
    @JsonProperty("merchantID")
    private String merchantID;

    private String token;

    public UpdateMenuNotificationRequestDTO(String merchantID) {
        this.merchantID = merchantID;
    }
}

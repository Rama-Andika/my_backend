package com.oxysystem.general.dto.posmaster.grabPackaging.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GrabPackagingItemDTO {
    @NotBlank(message = "product cannot be empty!")
    private String product;

    @NotEmpty(message = "packaging item cannot be empty!")
    private Long itemMasterId;

    @NotEmpty(message = "packaging location cannot be empty!")
    private Long locationId;
}

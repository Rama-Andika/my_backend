package com.oxysystem.general.dto.general.playgroundRegistration.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaygroundRegistrationViewDTO {
    private String id;
    private String regNumber;
    private String parentName;
    private LocalDateTime createdDate;
    private String status;
}

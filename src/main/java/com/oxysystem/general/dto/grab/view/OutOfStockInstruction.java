package com.oxysystem.general.dto.grab.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxysystem.general.enums.grab.InstructionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class OutOfStockInstruction {
    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String instructionType;

    private String replacementItemID;

    private String replacementGrabItemID;
}

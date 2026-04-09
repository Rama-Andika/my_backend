package com.oxysystem.general.dto.general.parent.data;

import com.oxysystem.general.dto.general.children.data.ChildrenDTO;
import com.oxysystem.general.dto.posmaster.payment.data.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentDTO {
    @NotBlank(message = "parent name cannot be empty!")
    private String name;

    private String phone;

    private String code;

    private String userId;

    private PaymentDTO payment;

    @Valid
    private List<ChildrenDTO> children;
}

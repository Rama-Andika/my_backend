package com.oxysystem.general.dto.general.children.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChildrenDTO {
    @NotBlank(message = "child name cannot be empty!")
    private String name;

    private Integer age;

    private String itemId;

    private Double price;

    private String code;
}

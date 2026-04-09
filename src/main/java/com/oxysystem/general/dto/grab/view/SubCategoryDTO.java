package com.oxysystem.general.dto.grab.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubCategoryDTO {
    private String id;

    private String name;

    private String availableStatus;

    private String sellingTimeID;

    private Integer sequence;

    private List<ItemDTO> items;
}

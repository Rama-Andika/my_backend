package com.oxysystem.general.dto.posmaster.itemGroup.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemGroupForSelectDTO {
    private String id;
    private String name;
    private List<ItemCategoryForSelectDTO> subCategories;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemCategoryForSelectDTO {
        private String id;
        private String name;
    }
}

package com.oxysystem.general.dto.posmaster.grabMartGroup.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GrabmartItemGroupForSelectDTO {
    private String id;
    private String name;
    private List<GrabmartItemCategoryForSelectDTO> subCategories;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class GrabmartItemCategoryForSelectDTO {
        private String id;
        private String name;
    }
}

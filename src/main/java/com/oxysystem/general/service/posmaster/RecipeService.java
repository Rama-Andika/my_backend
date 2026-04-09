package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.Recipe;

import java.util.List;

public interface RecipeService {
    List<Recipe> findByItemMasterIds(List<Long> itemMasterIds);
}

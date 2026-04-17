package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.tenant.posmaster.Recipe;
import com.oxysystem.general.repository.tenant.posmaster.RecipeRepository;
import com.oxysystem.general.service.posmaster.RecipeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> findByItemMasterIds(List<Long> itemMasterIds) {
        return recipeRepository.findByItemMasterIds(itemMasterIds);
    }
}

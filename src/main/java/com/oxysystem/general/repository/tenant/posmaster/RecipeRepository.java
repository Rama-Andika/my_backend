package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT r FROM Recipe r JOIN FETCH r.itemMaster im JOIN FETCH r.itemMasterRecipe imr WHERE im.itemMasterId IN :itemMasterIds")
    List<Recipe> findByItemMasterIds(@Param("itemMasterIds") List<Long> itemMasterIds);
}

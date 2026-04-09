package com.oxysystem.general.model.db1.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pos_recipe")
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "recipe_id")
    private Long recipeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_master_id")
    private ItemMaster itemMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_recipe_id")
    private ItemMaster itemMasterRecipe;

    private Double qty = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id")
    private Unit unit;

    private Double cost = 0.0;

    private String type = "";

    @Column(name = "last_update")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdate;

    private String description = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_recipe_id")
    private Unit unitRecipe;

    @Column(name = "conversi_recipe_qty")
    private Double conversiRecipeQty = 0.0;

    @Column(name = "qty_recipe")
    private Double qtyRecipe = 0.0;

    @Column(name = "last_update_by_id")
    private Long lastUpdateById;
}

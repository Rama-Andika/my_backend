package com.oxysystem.general.model.db1.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_item_master")
public class ItemMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "item_master_id")
    private Long itemMasterId;

    @ManyToOne
    @JoinColumn(name = "item_group_id")
    private ItemGroup itemGroup;

    @ManyToOne
    @JoinColumn(name = "item_category_id")
    private ItemCategory itemCategory;

    @Column(length = 120)
    private String name;

    @Column(length = 20)
    private String code;

    @Column(length = 20)
    private String barcode;

    @Column(name = "is_active")
    private Integer isActive = 0;

    @Column(name = "need_recipe")
    private Integer needRecipe = 0;

    @Column(name = "product_image")
    private String productImage;

    @ManyToOne
    @JoinColumn(name = "uom_stock_id")
    private Unit uomStock;

    @Column(name = "uom_stock_recipe_qty")
    private Double uomStockRecipeQty = 0.0;

    @Column(name = "is_bkp")
    private Integer isBkp = 0;

    @OneToMany(mappedBy = "itemMaster", fetch = FetchType.LAZY)
    private List<ItemMasterImage> itemMasterImages;

    @Column(name = "type_item")
    private int typeItem = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_vendor_id")
    private Vendor defaultVendor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemMaster")
    private List<VendorItem> vendorItems;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemMaster")
    private List<PriceType> priceTypes;
}

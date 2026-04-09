package com.oxysystem.general.model.db1.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.db1.posmaster.ItemGroup;
import com.oxysystem.general.model.db1.posmaster.ItemMaster;
import com.oxysystem.general.model.db1.posmaster.Vendor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pos_promotion_item")
public class PromotionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "promotion_item_id")
    private Long promotionItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_master_id")
    private ItemMaster itemMaster;

    @Column(name = "item_name")
    private String itemName = "";

    @Column(name = "item_code")
    private String itemCode = "";

    @Column(name = "item_barcode")
    private String itemBarcode = "";

    @Column(name = "discount_percent")
    private Double discountPercent = 0d;

    @Column(name = "discount_value")
    private Double discountValue = 0d;

    @Column(name = "selling_price")
    private Double sellingPrice = 0d;

    @Column(name = "tipe")
    private Integer tipe = 0;

    @Column(name = "qty_min")
    private Double qtyMin = 0d;

    @Column(name = "qty_bonus")
    private Double qtyBonus = 0d;

    @Column(name = "is_variant")
    private Integer isVariant = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_group_id")
    private ItemGroup itemGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @Column(name = "amount")
    private Double amount = 0d;

    @Column(name = "claim_percent")
    private Double claimPercent = 0d;

    @Column(name = "claim_amount")
    private Double claimAmount = 0d;

    @Column(name = "discount_percent_2")
    private Double discountPercent2 = 0d;

    @Column(name = "discount_value_2")
    private Double discountValue2 = 0d;

    @Column(name = "merk_id")
    private Long merkId = 0L;
}

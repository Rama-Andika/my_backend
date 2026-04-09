package com.oxysystem.general.model.db1.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oxysystem.general.model.db1.BaseEntity;
import com.oxysystem.general.model.db1.posmaster.ItemMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "pos_sales_taking_detail")
public class SalesTakingDetail extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    @JsonIgnoreProperties("salesTakingDetailList")
    private SalesTaking salesTaking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_master_id")
    private ItemMaster itemMaster;

    @Column(columnDefinition = "Decimal(20,2)")
    private Double cogs = 0.0;

    @Column(name = "selling_price", columnDefinition = "Decimal(20,2)")
    private Double sellingPrice = 0.0;

    @Column(columnDefinition = "Decimal(12,2)")
    private Double qty = 0.0;

    @Column(columnDefinition = "Decimal(20,2)")
    private Double total = 0.0;

    @Column(name = "discount_percent", columnDefinition = "Decimal(22,2)")
    private Double discountPercent = 0.0;

    @Column(name = "discount_amount", columnDefinition = "Decimal(22,2)")
    private Double discountAmount = 0.0;

    @Column(columnDefinition = "Decimal(4,2)")
    private Double tax = 0.0;

    @Column(name = "qty_retur", columnDefinition = "Decimal(25,2)")
    private Double qtyRetur = 0.0;

    @Column(name = "uom_id")
    private Long uomId;

    @Transient
    private String stringUomId;

    private String unit;

    @Column(name = "conv_qty", columnDefinition = "Decimal(10,2)")
    private Double convQty = 0.0;

    @Column(columnDefinition = "TEXT")
    private String note = "";

    private String description = "";

    @Column(name = "discount_item", columnDefinition = "Decimal(22,2)")
    private Double discountItem = 0.0;

    @Column(name = "discount_item_percent", columnDefinition = "Decimal(22,2)")
    private Double discountItemPercent = 0.0;

    @Column(name = "discount_promo", columnDefinition = "Decimal(22,2)")
    private Double discountPromo = 0.0;

    @Column(name = "discount_global", columnDefinition = "Decimal(22,2)")
    private Double discountGlobal = 0.0;

    @JsonIgnore
    private Integer status = 0;

    @Column(name = "company_id")
    private Long companyId = 0L;

    private Integer squence = 0;

    @Column(name = "short_name")
    private String shortName = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_detail_retur_id")
    private SalesTakingDetail salesTakingDetailReturn;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "salesTakingDetailReturn")
    private List<SalesTakingDetail> salesTakingDetailReturns =  new ArrayList<>();
}

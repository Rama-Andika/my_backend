package com.oxysystem.general.model.db1.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_vendor_item")
public class VendorItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "vendor_item_id")
    private Long vendorItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_master_id")
    private ItemMaster itemMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @Column(name = "last_price")
    private Double lastPrice = 0.0;

    @Column(name = "last_discount")
    private Double lastDiscount = 0.0;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "last_dis_val")
    private Double lastDisVal = 0.0;

    @Column(name = "reg_dis_percent")
    private Double regDisPercent = 0.0;

    @Column(name = "reg_dis_val")
    private Double regDisVal = 0.0;

    @Column(name = "real_price")
    private Double realPrice = 0.0;

    @Column(name = "margin_price")
    private Double marginPrice = 0.0;

    @Column(name = "item_vendor_code")
    private String itemVendorCode = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_purchase")
    private Unit uomPurchase;

    @Column(name = "delivery_unit")
    private Integer deliveryUnit = 0;

    @Column(name = "conv_qty")
    private Double convQty = 0.0;
}

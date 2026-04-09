package com.oxysystem.general.model.db1.transaction.sales;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.db1.general.Currency;
import com.oxysystem.general.model.db1.posmaster.ItemMaster;
import com.oxysystem.general.model.db1.posmaster.Unit;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pos_sales_detail")
public class SalesDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "sales_detail_id")
    private Long salesDetailId;

    @ManyToOne
    @JoinColumn(name = "sales_id")
    private Sales sales;

    @ManyToOne
    @JoinColumn(name = "product_master_id")
    private ItemMaster itemMaster;

    private Double cogs = 0.0;

    @Column(name = "selling_price")
    private Double sellingPrice = 0.0;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private Double qty = 0.0;

    @Column(name = "conv_qty")
    private Double convQty = 0.0;

    @ManyToOne
    @JoinColumn(name = "uom_id")
    private Unit unit;

    private BigDecimal total = BigDecimal.valueOf(0);

    @Column(name = "discount_percent")
    private BigDecimal discountPercent = BigDecimal.valueOf(0);

    @Column(name = "discount_amount")
    private BigDecimal discountAmount = BigDecimal.valueOf(0);

    @Column(name = "discount_item")
    private BigDecimal discountItem = BigDecimal.valueOf(0);

    @Column(name = "discount_promo")
    private BigDecimal discountPromo = BigDecimal.valueOf(0);

    @Column(name = "discount_global")
    private BigDecimal discountGlobal = BigDecimal.valueOf(0);

    @Column(name = "company_id")
    private Long companyId = 0L;

    private Double tax = 0.0;

    @Column(name = "status_komisi")
    private Integer statusKomisi = 0;

    private Integer bom = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_detail_retur_id")
    private SalesDetail salesDetailRetur;
}

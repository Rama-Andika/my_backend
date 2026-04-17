package com.oxysystem.general.model.tenant.transaction.sales;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_sales_grab_detail")
public class SalesGrabDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "sales_grab_detail_id")
    private Long salesGrabDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_grab_id")
    private SalesGrab salesGrab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_master_id")
    private ItemMaster itemMaster;

    @Column(name = "selling_price")
    private Double sellingPrice = 0.0;

    private Double qty = 0.0;

    private BigDecimal total = BigDecimal.valueOf(0);

    @Column(name = "verified_qty")
    private Double verifiedQty = 0.0;

    @Column(name = "is_verified")
    private Integer isVerified = 0;
}

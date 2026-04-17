package com.oxysystem.general.model.tenant.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.tenant.posmaster.Vendor;
import com.oxysystem.general.model.tenant.transaction.sales.SalesDetail;
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
@Table(name = "pos_sales_detail_promotion")
public class SalesDetailPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "sales_detail_promotion_id")
    private Long salesDetailPromotionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_detail_id")
    private SalesDetail salesDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Column(name = "type")
    private Integer type = 0;

    @Column(name = "discount_percent", columnDefinition = "DECIMAL(22,2)")
    private Double discountPercent = 0d;

    @Column(name = "discount_amount", columnDefinition = "DECIMAL(22,2)")
    private Double discountAmount = 0d;

    @Column(name = "claim_percent", columnDefinition = "DECIMAL(22,2)")
    private Double claimPercent = 0d;

    @Column(name = "claim_amount", columnDefinition = "DECIMAL(22,2)")
    private Double claimAmount = 0d;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;
}

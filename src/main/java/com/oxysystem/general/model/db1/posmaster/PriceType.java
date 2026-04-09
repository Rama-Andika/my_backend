package com.oxysystem.general.model.db1.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pos_price_type")
public class PriceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "price_type_id")
    private Long priceTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_master_id")
    private ItemMaster itemMaster;

    @Column(name = "qty_from")
    private Integer qtyFrom = 0;

    @Column(name = "qty_to")
    private Integer qtyTo = 0;

    @Column(name = "gol_1", columnDefinition = "DECIMAL(22,2)")
    private Double gol1 = 0.0;

    @Column(name = "gol_2", columnDefinition = "DECIMAL(22,2)")
    private Double gol2;

    @Column(name = "gol_3", columnDefinition = "DECIMAL(22,2)")
    private Double gol3;

    @Column(name = "gol_4", columnDefinition = "DECIMAL(22,2)")
    private Double gol4;

    @Column(name = "gol_5", columnDefinition = "DECIMAL(22,2)")
    private Double gol5;

    @Column(name = "gol1_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol1Margin;

    @Column(name = "gol2_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol2Margin;

    @Column(name = "gol3_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol3Margin;

    @Column(name = "gol4_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol4Margin;

    @Column(name = "gol5_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol5Margin;

    @Column(name = "change_date")
    private LocalDate changeDate;

    @Column(name = "gol_6", columnDefinition = "DECIMAL(22,2)")
    private Double gol6;

    @Column(name = "gol_7", columnDefinition = "DECIMAL(22,2)")
    private Double gol7;

    @Column(name = "gol_8", columnDefinition = "DECIMAL(22,2)")
    private Double gol8;

    @Column(name = "gol_9", columnDefinition = "DECIMAL(22,2)")
    private Double gol9;

    @Column(name = "gol_10", columnDefinition = "DECIMAL(22,2)")
    private Double gol10;

    @Column(name = "gol6_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol6Margin;

    @Column(name = "gol7_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol7Margin;

    @Column(name = "gol8_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol8Margin;

    @Column(name = "gol9_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol9Margin;

    @Column(name = "gol10_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol10Margin;

    @Column(name = "gol_11", columnDefinition = "DECIMAL(22,2)")
    private Double gol11;

    @Column(name = "gol11_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol11Margin;

    @Column(name = "harga_maksimum", columnDefinition = "DECIMAL(22,2)")
    private Double hargaMaksimum;

    @Column(name = "harga_normal", columnDefinition = "DECIMAL(22,2)")
    private Double hargaNormal;

    @Column(name = "harga_minimum", columnDefinition = "DECIMAL(22,2)")
    private Double hargaMinimum;

    @Column(name = "gol_12", columnDefinition = "DECIMAL(22,2)")
    private Double gol12;

    @Column(name = "gol_13", columnDefinition = "DECIMAL(22,2)")
    private Double gol13;

    @Column(name = "gol_14", columnDefinition = "DECIMAL(22,2)")
    private Double gol14;

    @Column(name = "gol_15", columnDefinition = "DECIMAL(22,2)")
    private Double gol15;

    @Column(name = "gol12_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol12Margin;

    @Column(name = "gol13_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol13Margin;

    @Column(name = "gol14_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol14Margin;

    @Column(name = "gol15_margin", columnDefinition = "DECIMAL(22,2)")
    private Double gol15Margin;

    @Column(name = "uom_id")
    private Long uomId;

    @Column(name = "conv_qty")
    private Double convQty;

    // Getters and Setters
    // Bisa generate pakai Lombok @Data jika mau

}

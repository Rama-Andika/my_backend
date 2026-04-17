package com.oxysystem.general.model.tenant.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vendor")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "vendor_id")
    private Long vendorId;

    private String name = "";

    private Integer type = 0;

    @Column(name = "is_konsinyasi")
    private Integer isKonsinyasi = 0;

    @Column(name = "is_pkp")
    private Integer isPKP = 0;

    @Column(name = "include_ppn")
    private Integer includePPN = 0;

    @Column(name = "komisi_percent")
    private Double komisiPercent = 0.0;

    @Column(name = "percent_margin")
    private Double percentMargin = 0.0;

    @Column(name = "percent_promosi")
    private Double percentPromosi = 0.0;

    @Column(name = "percent_barcode")
    private Double percentBarcode = 0.0;

    private Integer system = 0;

    @Column(name = "komisi_margin")
    private Double komisiMargin = 0.0;

    @Column(name = "komisi_promosi")
    private Double komisiPromosi = 0.0;

    @Column(name = "komisi_barcode")
    private Double komisiBarcode = 0.0;

    @Column(name = "is_komisi")
    private Integer isKomisi = 0;
}

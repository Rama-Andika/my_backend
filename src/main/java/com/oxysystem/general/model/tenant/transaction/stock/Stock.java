package com.oxysystem.general.model.tenant.transaction.stock;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.model.tenant.posmaster.Unit;
import com.oxysystem.general.model.tenant.transaction.sales.SalesDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "stock_id")
    private Long stockId;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private Integer type;

    private Double qty;

    private Double price;

    private Double total;

    @ManyToOne
    @JoinColumn(name = "item_master_id")
    private ItemMaster itemMaster;

    @Column(name = "item_code")
    private String itemCode;

    @Column(name = "item_barcode")
    private String itemBarcode;

    @Column(name = "item_name")
    private String itemName;

    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "uom_id")
    private Unit uom;

    private String unit;

    @Column(name = "in_out")
    private Integer inOut;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "no_faktur")
    private String noFaktur;

    @Column(name = "opname_id")
    private Long opnameId;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @Column(name = "owner_id", length = 20)
    private Long ownerId;

    @Column(name = "type_item")
    private Integer typeItem = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_detail_id")
    private SalesDetail salesDetail;
}

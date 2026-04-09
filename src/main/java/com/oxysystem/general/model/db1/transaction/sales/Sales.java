package com.oxysystem.general.model.db1.transaction.sales;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.enums.SalesType;
import com.oxysystem.general.model.db1.admin.User;
import com.oxysystem.general.model.db1.general.Currency;
import com.oxysystem.general.model.db1.general.Customer;
import com.oxysystem.general.model.db1.general.Location;
import com.oxysystem.general.model.db1.posmaster.CashCashier;
import com.oxysystem.general.model.db1.posmaster.CashMaster;
import com.oxysystem.general.model.db1.posmaster.Shift;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pos_sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "sales_id")
    private Long salesId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Column(length = 70, unique = true)
    private String number;

    @Column(name = "number_prefix", length = 20)
    private String numberPrefix = "";

    private Integer counter = 0;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer status = 0;

    private Double amount = 0.0;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Column(name = "discount_percent")
    private Double discountPercent = 0.0;

    @Column(name = "discount_amount")
    private Double discountAmount = 0.0;

    @Column(name = "global_diskon")
    private Double globalDiskon = 0.0;

    @Column(name = "global_diskon_percent")
    private Double globalDiskonPercent = 0.0;

    private Integer vat = 0;

    private Double discount = 0.0;

    @Enumerated(EnumType.ORDINAL)
    private SalesType type;

    @Column(name = "sales_type")
    private Integer salesType = 0;


    @Column(name = "payment_status")
    private Integer paymentStatus = 0;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "cash_cashier_id")
    private CashCashier cashCashier;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "cash_master_id")
    private CashMaster cashMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_retur_id")
    private Sales salesRetur;

    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL)
    private List<SalesDetail> salesDetails;
}

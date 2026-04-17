package com.oxysystem.general.model.tenant.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oxysystem.general.enums.SalesType;
import com.oxysystem.general.model.tenant.BaseEntity;
import com.oxysystem.general.model.tenant.general.Customer;
import com.oxysystem.general.model.tenant.general.Location;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "pos_sales_taking")
public class SalesTaking extends BaseEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "DATETIME(0)")
    private LocalDateTime date;

    @Column(unique = true)
    private String number = "";

    @Column(name = "number_prefix")
    @JsonIgnore
    private String numberPrefix = "";

    @JsonIgnore
    private Integer counter = 0;

    @JsonIgnore
    @Column(name = "return_counter")
    private Integer returnCounter = 0;

    private String name = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "employee_id")
    private Long employeeId = 0L;

    @Column(name = "user_id")
    @JsonIgnore
    private Long userId = 0L;

    @Column(columnDefinition = "TEXT")
    private String description = "";

    private Integer status = 0;

    @Column(name = "doc_status", nullable = false)
    private String docStatus;

    @Column(columnDefinition = "Decimal(25,2)")
    private Double amount = 0.0;

    @Column(name = "discount_percent", columnDefinition = "Decimal(22,2)")
    private Double discountPercent = 0.0;

    @Column(name = "discount_amount", columnDefinition = "Decimal(22,2)")
    private Double discountAmount = 0.0;

    private Integer vat = 0;

    private Integer discount = 0;

    @Column(name = "vat_percent")
    private Double vatPercent = 0.0;

    @Column(name = "vat_amount",columnDefinition = "Decimal(25,2)")
    private Double vatAmount = 0.0;

    @Enumerated(EnumType.ORDINAL)
    private SalesType type = SalesType.CASH;

    @Column(name = "marketing_id")
    private Long marketingId = 0L;

    @Column(name = "payment_status")
    private Integer paymentStatus = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "cash_cashier_id")
    @JsonIgnore
    private Long cashCashierId = 0L;

    @Column(name = "global_diskon", columnDefinition = "Decimal(10,2)")
    private Double globalDiskon = 0.0;

    @Column(name = "global_diskon_percent", columnDefinition = "Decimal(10,2)")
    private Double globalDiskonPercent = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_retur_id")
    private SalesTaking salesTakingReturn;

    @OneToMany(mappedBy = "salesTaking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("salesTaking")
    @OrderBy("createdAt asc ")
    private List<SalesTakingDetail> salesTakingDetailList;
}

package com.oxysystem.general.model.tenant.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.model.tenant.general.Currency;
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
@Table(name = "pos_cash_cashier")
public class CashCashier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "cash_cashier_id")
    private Long cashCashierId;

    @ManyToOne
    @JoinColumn(name = "cash_master_id")
    private CashMaster cashMaster;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_open")
    private LocalDateTime dateOpen;

    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID_OPEN")
    private Currency currencyOpen;

    @ManyToOne
    @JoinColumn(name = "CURRENCY_ID_CLOSING")
    private Currency currencyClosing;

    @Column(name = "rate_open")
    private Double rateOpen = 0.0;

    @Column(name = "amount_open")
    private Double amountOpen = 0.0;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "date_closing")
    private LocalDateTime dateClosing;

    @Column(name = "rate_closing")
    private Double rateClosing = 0.0;

    @Column(name = "amount_closing")
    private Double amountClosing = 0.0;

    private Integer status = 0;

    @Column(name = "should_value")
    private Double shouldValue = 0.0;

    @Column(name = "count_sales")
    private Integer countSales = 0;

    @Column(name = "count_detail")
    private Integer countDetail = 0;

    @Column(name = "count_payment")
    private Integer countPayment = 0;
}

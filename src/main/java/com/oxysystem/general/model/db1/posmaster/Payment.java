package com.oxysystem.general.model.db1.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.db1.general.Bank;
import com.oxysystem.general.model.db1.general.Currency;
import com.oxysystem.general.model.db1.general.Merchant;
import com.oxysystem.general.model.db1.general.PaymentMethod;
import com.oxysystem.general.model.db1.transaction.sales.Sales;
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
@Table(name = "pos_payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "payment_id")
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "sales_id")
    private Sales sales;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "pay_date")
    private LocalDateTime payDate;

    @Column(name = "pay_type")
    private Integer payType = 0;

    private Double amount = 0.0;

    private Double rate = 0.0;

    @Column(name = "cost_card_amount", columnDefinition = "DECIMAL(20,2)")
    private Double costCardAmount = 0.0;

    @Column(name = "cost_card_percent", columnDefinition = "DECIMAL(20,2)")
    private Double costCardPercent = 0.0;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
}

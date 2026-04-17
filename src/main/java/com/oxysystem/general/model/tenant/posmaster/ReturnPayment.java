package com.oxysystem.general.model.tenant.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.tenant.general.Currency;
import com.oxysystem.general.model.tenant.transaction.sales.Sales;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_return_payment")
public class ReturnPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "return_payment_id")
    private Long returnPaymentId;

    @ManyToOne
    @JoinColumn(name = "sales_id")
    private Sales sales;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private Double amount = 0.0;
}

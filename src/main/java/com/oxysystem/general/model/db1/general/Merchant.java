package com.oxysystem.general.model.db1.general;

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
@Table(name = "merchant")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "merchant_id")
    private Long merchantId;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    private String description;

    @Column(name = "payment_by")
    private Integer paymentBy;

    @Column(name = "persen_expense")
    private Double persenExpense = 0.0;

    @Column(name = "posting_expense")
    private Integer postingExpense = 0;
}

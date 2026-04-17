package com.oxysystem.general.model.tenant.general;

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
@Table(name = "bank")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "bank_id")
    private Long bankId;

    private String name;

    @Column(name = "default_bunga")
    private Double defaultBunga;

    @Column(name = "coa_ar_id")
    private Long coaArId;

    @Column(name = "coa_ap_id")
    private Long coaApId;

    @Column(name = "coa_debit_card_id")
    private Long coaDebitCardId;
}

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
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "currency_id")
    private Long currencyId;

    @Column(name = "currency_code", length = 20)
    private String currencyCode;

    @Column(length = 120)
    private String description;

    @Column(name = "company_id", length = 20)
    private Long companyId;

    private Double rate;

    @Column(name = "coa_id", length = 20)
    private Long coaId;
}

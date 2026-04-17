package com.oxysystem.general.model.tenant.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.enums.CashMasterType;
import com.oxysystem.general.model.tenant.general.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_cash_master")
public class CashMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "cash_master_id")
    private Long cashMasterId;

    @Column(name = "cashier_number")
    private Integer cashierNumber = 0;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "company_id")
    private Long companyId = 0L;

    @Column(name = "sts_loc")
    private String stsLoc;

    @Enumerated(EnumType.ORDINAL)
    private CashMasterType type;
}

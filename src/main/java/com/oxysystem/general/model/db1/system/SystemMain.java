package com.oxysystem.general.model.db1.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "system_main")
public class SystemMain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "sysmain_id")
    private Long sysmainId;

    @Column(length = 120)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String valueprop;

    @Column(length = 64)
    private String valtype;

    @Column(length = 64)
    private String groupprop;

    @Column(length = 120)
    private String note;

    @Column(name = "company_id")
    private Long companyId;

    private Integer active;
}

package com.oxysystem.general.model.db1.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "location_id")
    private Long locationId;

    @Column(length = 120)
    private String name;

    @Column(name = "gol_price", length = 45)
    private String golPrice;

    @Column(name = "is_active")
    private Integer isActive = 0;
}

package com.oxysystem.general.model.tenant.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.tenant.general.Location;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "pos_promotion_grab_location")
public class PromotionGrabLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "id")
    private Long id;

    @Column(name = "promotion_id_by_grab")
    private String promotionIdByGrab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_grab_id")
    private PromotionGrab promotionGrab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}

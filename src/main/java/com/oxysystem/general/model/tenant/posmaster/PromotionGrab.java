package com.oxysystem.general.model.tenant.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.model.tenant.admin.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pos_promotion_grab")
public class PromotionGrab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(name = "number_prefix")
    private String numberPrefix;

    private Integer counter = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime endTime;

    @Column(name = "eater_type")
    private String eaterType;

    @Column(name = "min_basket_amount")
    private Double minBasketAmount = 0.0;

    @Column(name = "bundleQuantity")
    private Integer bundleQuantity = 0;

    @Column(name = "total_count")
    private Integer totalCount = 0;

    @Column(name = "total_count_per_user")
    private Integer totalCountPerUser = 0;

    private String type;

    private Double cap = 0.0;

    private Double value = 0.0;

    @Column(name = "scope_type")
    private String scopeType;

    @Column(name = "custom_tag")
    private String customTag;

    @Column(name = "created_by")
    private String createdBy;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private String product;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_date",columnDefinition = "DATETIME(0)")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_date",columnDefinition = "DATETIME(0)")
    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "promotionGrab", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PromotionGrabDetail> promotionGrabDetails;

    @OneToMany(mappedBy = "promotionGrab", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PromotionGrabLocation> promotionGrabLocations;

    @PrePersist
    void prePersist(){
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate(){
        this.updatedDate = LocalDateTime.now();
    }
}

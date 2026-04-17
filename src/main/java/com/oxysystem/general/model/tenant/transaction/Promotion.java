package com.oxysystem.general.model.tenant.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.tenant.admin.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pos_promotion")
@Entity
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(name = "start_date")
    private LocalDateTime startDate = null;

    @Column(name = "end_date")
    private LocalDateTime endDate = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(name = "user_name")
    private String userName = "";

    @Column(name = "promo_desc")
    private String promoDesc = "";

    @Column(name = "counter")
    private Integer counter = 0;

    @Column(name = "number")
    private String number = "";

    @Column(name = "status")
    private String status = "";

    @Column(name = "prefix_number")
    private String prefixNumber = "";

    @Column(name = "tipe")
    private Integer tipe = 0;

    @Column(name = "jenis")
    private Integer jenis = 0;

    @Column(name = "sub_type")
    private Integer subType = 0;

    @Column(name = "for_customer")
    private Integer forCustomer = 0;

    @Column(name = "include_discount_customer")
    private Integer includeDiscountCustomer = 0;

    @Column(name = "start_time")
    private LocalTime startTime = null;

    @Column(name = "end_time")
    private LocalTime endTime = null;

    @Column(name = "created_at")
    private LocalDateTime createdAt = null;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = null;

    @Column(name = "path_image")
    private String pathImage = "";

    @PrePersist
    public void onPrePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}
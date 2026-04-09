package com.oxysystem.general.model.db1.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "grabmart_item_category")
public class GrabmartItemCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(name = "grabmart_item_category_id")
    private String grabmartItemCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grabmart_item_group_id")
    private GrabmartItemGroup grabmartItemGroup;

    private Integer sequence;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public GrabmartItemCategory(String name, String grabmartItemCategoryId, GrabmartItemGroup grabmartItemGroup) {
        this.name = name;
        this.grabmartItemCategoryId = grabmartItemCategoryId;
        this.grabmartItemGroup = grabmartItemGroup;
    }

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

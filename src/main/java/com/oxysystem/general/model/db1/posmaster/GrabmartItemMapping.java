package com.oxysystem.general.model.db1.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.db1.general.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "grabmart_item_mapping")
public class GrabmartItemMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grabmart_item_group_id")
    private GrabmartItemGroup grabmartItemGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grabmart_item_category_id")
    private GrabmartItemCategory grabmartItemCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_master_id")
    private ItemMaster itemMaster;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "grabmart_unit")
    private String grabmartUnit;

    @Column(columnDefinition = "DECIMAL(22,2)")
    private Double value;

    @Column(name = "special_type")
    private String specialType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_published")
    private Integer isPublished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

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

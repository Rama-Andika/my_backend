package com.oxysystem.general.model.db1.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.oxysystem.general.model.db1.general.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "grabfood_item_mapping")
public class GrabfoodItemMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grabfood_item_group_id")
    private GrabfoodItemGroup grabfoodItemGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_master_id")
    private ItemMaster itemMaster;

    @Column(name = "item_name")
    private String itemName;

    private String barcode;

    @Column(columnDefinition = "TEXT")
    private String description;

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

package com.oxysystem.general.model.tenant.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "grabmart_item_group")
public class GrabmartItemGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(name = "grabmart_item_group_id")
    private String grabmartItemGroupId;

    private Integer sequence;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "grabmartItemGroup", fetch = FetchType.LAZY)
    private List<GrabmartItemCategory> grabmartItemCategories;

    public GrabmartItemGroup(String name, String grabmartItemGroupId) {
        this.name = name;
        this.grabmartItemGroupId = grabmartItemGroupId;
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

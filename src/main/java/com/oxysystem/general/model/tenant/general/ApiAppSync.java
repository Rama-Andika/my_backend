package com.oxysystem.general.model.tenant.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "api_app_sync", indexes = {
        @Index(name = "idx_tablename_status", columnList = "table_name, status")
})
public class ApiAppSync {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "api_sync_id")
    private Long apiSyncId;

    private LocalDateTime date;

    @Column(name = "table_name")
    private String tableName = "";

    private String action = "";

    @Column(name = "owner_id")
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private ApiApp apiApp;

    private Integer count = 0;

    private Integer status = 1;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @PrePersist
    void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }
}

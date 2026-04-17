package com.oxysystem.general.model.tenant.posmaster;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "pos_item_master_image")
public class ItemMasterImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "item_master_image_id")
    private Long itemMasterImageId;

    @ManyToOne
    @JoinColumn(name = "item_master_id")
    private ItemMaster itemMaster;

    @Column(name = "file_name", columnDefinition = "TEXT")
    private String fileName;

    @Column(name = "original_name", columnDefinition = "TEXT")
    private String originalName;

    @Column(columnDefinition = "TEXT")
    private String note;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    private Integer sequence;

    @Column(columnDefinition = "TEXT")
    private String path;
}

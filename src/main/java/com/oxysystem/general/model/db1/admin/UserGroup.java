package com.oxysystem.general.model.db1.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "grp")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "group_name")
    private String groupName = "";

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(columnDefinition = "TEXT")
    private String description = "";
}

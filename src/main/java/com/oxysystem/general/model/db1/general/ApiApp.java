package com.oxysystem.general.model.db1.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "api_app")
public class ApiApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "api_app_id")
    private Long apiAppId;

    private String name = "";

    private Integer status = 0;

    @OneToMany(mappedBy = "apiApp", fetch = FetchType.LAZY)
    private List<ApiAppSyncSetup> apiAppSyncSetups;
}

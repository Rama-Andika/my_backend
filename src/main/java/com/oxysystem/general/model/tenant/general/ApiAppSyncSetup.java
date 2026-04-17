package com.oxysystem.general.model.tenant.general;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "api_app_sync_setup")
public class ApiAppSyncSetup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "secure-id")
    @GenericGenerator(name = "secure-id", strategy = "com.oxysystem.general.util.GenerateRandomIdUtils")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "api_sync_setup_id")
    private Long apiSyncSetupId;

    @Column(name = "table_name")
    private String tableName = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    private ApiApp apiApp;

    private Integer status = 0;
}

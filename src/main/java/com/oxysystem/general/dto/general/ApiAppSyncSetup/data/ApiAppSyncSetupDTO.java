package com.oxysystem.general.dto.general.ApiAppSyncSetup.data;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiAppSyncSetupDTO {
    private String tableName;

    private Long appId;

    private Integer status;
}

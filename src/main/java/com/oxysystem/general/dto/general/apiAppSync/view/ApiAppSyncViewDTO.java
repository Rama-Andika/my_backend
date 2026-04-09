package com.oxysystem.general.dto.general.apiAppSync.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiAppSyncViewDTO {
    private Long apiSyncId;

    private LocalDateTime date;

    private String tableName = "";

    private String action = "";

    private Long ownerId;

    private Integer status = 1;

    private Long locationId;

    private String grabMerchantId;
}

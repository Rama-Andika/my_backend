package com.oxysystem.general.dto.general.apiAppSync.data;

import com.oxysystem.general.model.db1.general.Location;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ApiAppSyncDTO {
    private Long apiSyncId;

    private LocalDateTime date;

    private String tableName = "";

    private String action = "";

    private Long ownerId;

    private Integer count = 0;

    private Integer status = 1;

    private Location location;
}

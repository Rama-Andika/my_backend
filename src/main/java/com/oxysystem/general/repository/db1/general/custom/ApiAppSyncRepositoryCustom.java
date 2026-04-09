package com.oxysystem.general.repository.db1.general.custom;

import com.oxysystem.general.dto.general.apiAppSync.view.ApiAppSyncViewDTO;

import java.util.List;

public interface ApiAppSyncRepositoryCustom {
    List<ApiAppSyncViewDTO> findApiAppSyncGrabNotSyncByTableName(String tableName);
    List<ApiAppSyncViewDTO> findApiAppSyncGrabFoodNotSyncByTableName(String tableName);
}

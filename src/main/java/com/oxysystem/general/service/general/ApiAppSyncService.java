package com.oxysystem.general.service.general;

import com.oxysystem.general.dto.general.apiAppSync.data.ApiAppSyncDTO;
import com.oxysystem.general.dto.general.apiAppSync.view.ApiAppSyncViewDTO;
import com.oxysystem.general.model.db1.general.ApiApp;
import com.oxysystem.general.model.db1.general.ApiAppSync;

import java.util.List;

public interface ApiAppSyncService {
    void saveAll(List<ApiAppSync> apiAppSyncs);
    void batchSaveApiAppSync(List<ApiAppSyncDTO> bodies, ApiApp apiApp);
    List<ApiAppSyncViewDTO> findApiAppSyncGrabNotSyncByTableName(String tableName);
    List<ApiAppSyncViewDTO> findApiAppSyncGrabFoodNotSyncByTableName(String tableName);
    void updateApiAppSyncStatusByApiSyncIds(Integer status, List<Long> apiSyncIds);
    void updateApiAppSyncStatusByApiSyncId(Integer status, Long apiSyncId);
    void deleteApiAppSyncByTableNameAndOwnerIdAndLocationIds(Integer status, String tableName, Long ownerId, List<Long> locationIds);
    // void saveActiveApiAppSyncAll(String tableName, String action, Long ownerId, Long apiAppId);
}

package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.apiAppSync.data.ApiAppSyncDTO;
import com.oxysystem.general.dto.general.apiAppSync.view.ApiAppSyncViewDTO;
import com.oxysystem.general.model.tenant.general.ApiApp;
import com.oxysystem.general.model.tenant.general.ApiAppSync;
import com.oxysystem.general.repository.tenant.general.ApiAppSyncRepository;
import com.oxysystem.general.service.general.ApiAppSyncService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiAppSyncServiceImpl implements ApiAppSyncService {
    private final ApiAppSyncRepository apiAppSyncRepository;

    public ApiAppSyncServiceImpl(ApiAppSyncRepository apiAppSyncRepository) {
        this.apiAppSyncRepository = apiAppSyncRepository;
    }
    @Override
    public void saveAll(List<ApiAppSync> apiAppSyncs) {
        apiAppSyncRepository.saveAll(apiAppSyncs);
    }

    @Override
    public void batchSaveApiAppSync(List<ApiAppSyncDTO> bodies, ApiApp apiApp) {
        if(bodies != null && !bodies.isEmpty()){
            List<ApiAppSync> apiAppSyncs = new ArrayList<>();

            for(ApiAppSyncDTO body: bodies){
                ApiAppSync apiAppSync = new ApiAppSync();
                apiAppSync.setDate(LocalDateTime.now());
                apiAppSync.setTableName(body.getTableName());
                apiAppSync.setAction(body.getAction());
                apiAppSync.setOwnerId(body.getOwnerId());
                apiAppSync.setApiApp(apiApp);
                apiAppSync.setCount(3);
                apiAppSync.setStatus(0);
                apiAppSync.setLocation(body.getLocation());

                apiAppSyncs.add(apiAppSync);
            }

            apiAppSyncRepository.saveAll(apiAppSyncs);
        }
    }

    @Override
    public List<ApiAppSyncViewDTO> findApiAppSyncGrabNotSyncByTableName(String tableName) {
        return apiAppSyncRepository.findApiAppSyncGrabNotSyncByTableName(tableName);
    }

    @Override
    public List<ApiAppSyncViewDTO> findApiAppSyncGrabFoodNotSyncByTableName(String tableName) {
        return apiAppSyncRepository.findApiAppSyncGrabFoodNotSyncByTableName(tableName);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public void updateApiAppSyncStatusByApiSyncIds(Integer status, List<Long> apiSyncIds) {
        apiAppSyncRepository.updateApiAppSyncStatusByApiSyncIds(status, apiSyncIds);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public void updateApiAppSyncStatusByApiSyncId(Integer status, Long apiSyncId) {
        apiAppSyncRepository.updateApiAppSyncStatusByApiSyncId(status, apiSyncId);
    }
    @Override
    public void deleteApiAppSyncByTableNameAndOwnerIdAndLocationIds(Integer status, String tableName, Long ownerId, List<Long> locationIds) {
        apiAppSyncRepository.deleteApiAppSyncByTableNameAndOwnerIdAndLocationIds(status, tableName, ownerId, locationIds);
    }
}

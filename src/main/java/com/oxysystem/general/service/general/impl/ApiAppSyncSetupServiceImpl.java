package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.ApiAppSyncSetup.data.ApiAppSyncSetupDTO;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.db1.general.ApiApp;
import com.oxysystem.general.model.db1.general.ApiAppSync;
import com.oxysystem.general.model.db1.general.ApiAppSyncSetup;
import com.oxysystem.general.repository.db1.general.ApiAppSyncSetupRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.ApiAppService;
import com.oxysystem.general.service.general.ApiAppSyncSetupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiAppSyncSetupServiceImpl implements ApiAppSyncSetupService {
    private final ApiAppSyncSetupRepository apiAppSyncSetupRepository;
    private final ApiAppService apiAppService;

    public ApiAppSyncSetupServiceImpl(ApiAppSyncSetupRepository apiAppSyncSetupRepository, ApiAppService apiAppService) {
        this.apiAppSyncSetupRepository = apiAppSyncSetupRepository;
        this.apiAppService = apiAppService;
    }

    @Override
    public ResponseEntity<?> save(ApiAppSyncSetupDTO body) {
        ApiAppSyncSetup apiAppSyncSetup = new ApiAppSyncSetup();
        apiAppSyncSetup.setTableName(body.getTableName());

        ApiApp apiApp = apiAppService.findById(body.getAppId()).orElseThrow(() -> new ResourceNotFoundException("api app not found!"));
        apiAppSyncSetup.setApiApp(apiApp);
        apiAppSyncSetup.setStatus(body.getStatus());

        apiAppSyncSetup = apiAppSyncSetupRepository.save(apiAppSyncSetup);
        SuccessResponse<String> response = new SuccessResponse<>("success", String.valueOf(apiAppSyncSetup.getApiSyncSetupId()));
        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<ApiAppSyncSetup> findById(Long id) {
        return apiAppSyncSetupRepository.findById(id);
    }

    @Override
    public List<ApiAppSyncSetup> findApiAppSyncSetupActiveByApiApp(Long apiAppId) {
        return apiAppSyncSetupRepository.findApiAppSyncSetupActiveByApiApp(apiAppId);
    }
}

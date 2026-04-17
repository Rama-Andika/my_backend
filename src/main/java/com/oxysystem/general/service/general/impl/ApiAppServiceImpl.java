package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.apiApp.data.ApiAppDTO;
import com.oxysystem.general.model.tenant.general.ApiApp;
import com.oxysystem.general.repository.tenant.general.ApiAppRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.ApiAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApiAppServiceImpl implements ApiAppService {
    private final ApiAppRepository apiAppRepository;

    public ApiAppServiceImpl(ApiAppRepository apiAppRepository) {
        this.apiAppRepository = apiAppRepository;
    }

    @Override
    public ResponseEntity<?> save(ApiAppDTO data) {
        ApiApp apiApp = new ApiApp();
        apiApp.setName(data.getName());
        apiApp.setStatus(data.getStatus());

        apiApp = apiAppRepository.save(apiApp);

        SuccessResponse<String> response = new SuccessResponse<>("success", String.valueOf(apiApp.getApiAppId()));
        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<ApiApp> findById(Long id) {
        return apiAppRepository.findById(id);
    }

    @Override
    public List<ApiApp> findAllById(List<Long> ids) {
        return apiAppRepository.findAllById(ids);
    }

    @Override
    public Optional<ApiApp> findApiAppByName(String name) {
        return apiAppRepository.findApiAppByName(name);
    }
}

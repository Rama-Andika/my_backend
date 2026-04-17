package com.oxysystem.general.service.general;

import com.oxysystem.general.dto.general.ApiAppSyncSetup.data.ApiAppSyncSetupDTO;
import com.oxysystem.general.model.tenant.general.ApiAppSyncSetup;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ApiAppSyncSetupService {
    ResponseEntity<?> save(ApiAppSyncSetupDTO body);

    Optional<ApiAppSyncSetup> findById(Long id);
    List<ApiAppSyncSetup> findApiAppSyncSetupActiveByApiApp(Long apiAppId);
}

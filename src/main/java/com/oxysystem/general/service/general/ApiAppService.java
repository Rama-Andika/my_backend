package com.oxysystem.general.service.general;

import com.oxysystem.general.dto.general.apiApp.data.ApiAppDTO;
import com.oxysystem.general.model.db1.general.ApiApp;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ApiAppService {
    ResponseEntity<?> save(ApiAppDTO data);
    Optional<ApiApp> findById(Long id);
    List<ApiApp> findAllById(List<Long> ids);
    Optional<ApiApp> findApiAppByName(String name);
}

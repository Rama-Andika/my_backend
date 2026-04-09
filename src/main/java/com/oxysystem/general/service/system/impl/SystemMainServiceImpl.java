package com.oxysystem.general.service.system.impl;

import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.db1.system.SystemMain;
import com.oxysystem.general.repository.db1.system.SystemMainRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.system.SystemMainService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Service
public class SystemMainServiceImpl implements SystemMainService {
    private final SystemMainRepository systemMainRepository;

    public SystemMainServiceImpl(SystemMainRepository systemMainRepository) {
        this.systemMainRepository = systemMainRepository;
    }

    @Override
    public Optional<SystemMain> findSystemPropertyName(String name) {
        return systemMainRepository.findSystemPropertyName(name);
    }

    @Override
    public Mono<Optional<SystemMain>> findSystemPropertyNameReactive(String name) {
        return Mono.fromCallable(() -> systemMainRepository.findSystemPropertyName(name)).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public ResponseEntity<?> findSystemPropertyNameForApi(String name) {
        SystemMain systemMain = systemMainRepository.findSystemPropertyName(name).orElseThrow(() -> new ResourceNotFoundException("Configuration not found"));

        SuccessResponse<?> response = new SuccessResponse<>("success", systemMain);
        return ResponseEntity.ok(response);
    }
}

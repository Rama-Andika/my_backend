package com.oxysystem.general.service.system;

import com.oxysystem.general.model.tenant.system.SystemMain;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface SystemMainService {
    Optional<SystemMain> findSystemPropertyName(String name);
    Mono<Optional<SystemMain>> findSystemPropertyNameReactive(String name);
    ResponseEntity<?> findSystemPropertyNameForApi(String name);
}

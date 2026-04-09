package com.oxysystem.general.service.general;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface DashboardGrabService {
    Mono<Void> saveAsync(int type, String product);
    ResponseEntity<?> getDashboardSummaryGrab(String product);
    ResponseEntity<?> getRecentSalesByCashMasterType(int type);
}

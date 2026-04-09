package com.oxysystem.general.service.transaction;

import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface SalesTakingDetailService {
    ResponseEntity<?> deleteSalesTakingDetailById(Long salesTakingDetailId);
    ResponseEntity<?> deleteSalesTakingDetailByIds(Set<Long> salesTakingDetailIds);
    ResponseEntity<?> findById(Long salesTakingDetailId);
    ResponseEntity<?> findByIds(Set<Long> salesTakingDetailId);
}

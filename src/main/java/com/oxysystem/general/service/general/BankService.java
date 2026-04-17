package com.oxysystem.general.service.general;

import com.oxysystem.general.model.tenant.general.Bank;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface BankService {
    Optional<Bank> findById(Long id);
    ResponseEntity<?> getBanks();
}

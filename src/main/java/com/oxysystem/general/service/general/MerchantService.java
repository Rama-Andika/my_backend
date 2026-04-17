package com.oxysystem.general.service.general;

import com.oxysystem.general.model.tenant.general.Merchant;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface MerchantService {
    Optional<Merchant> findById(Long id);
    Optional<Merchant> findMerchantByLocationAndDescription(String description, long locationId);
    ResponseEntity<?> getMerchantsByLocationId(Long locationId);
}

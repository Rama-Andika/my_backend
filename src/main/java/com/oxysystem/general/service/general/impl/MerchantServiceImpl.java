package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.merchant.view.MerchantViewDTO;
import com.oxysystem.general.model.db1.general.Merchant;
import com.oxysystem.general.repository.db1.general.MerchantRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.MerchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;

    public MerchantServiceImpl(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Override
    public Optional<Merchant> findById(Long id) {
        return merchantRepository.findById(id);
    }

    @Override
    public Optional<Merchant> findMerchantByLocationAndDescription(String description, long locationId) {
        return merchantRepository.findMerchantByLocationAndDescription(description, locationId);
    }

    @Override
    public ResponseEntity<?> getMerchantsByLocationId(Long locationId) {
        List<MerchantViewDTO> merchants = merchantRepository.getMerchantsByLocationId(locationId);
        SuccessResponse<?> response = new SuccessResponse<>("success", merchants);
        return ResponseEntity.ok(response);
    }
}

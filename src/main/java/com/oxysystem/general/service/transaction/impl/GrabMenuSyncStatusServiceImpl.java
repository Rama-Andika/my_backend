package com.oxysystem.general.service.transaction.impl;

import com.oxysystem.general.model.tenant.transaction.GrabMenuSyncStatus;
import com.oxysystem.general.repository.tenant.transaction.GrabMenuSyncStatusRepository;
import com.oxysystem.general.service.transaction.GrabMenuSyncStatusService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GrabMenuSyncStatusServiceImpl implements GrabMenuSyncStatusService {
    private final GrabMenuSyncStatusRepository grabMenuSyncStatusRepository;

    public GrabMenuSyncStatusServiceImpl(GrabMenuSyncStatusRepository grabMenuSyncStatusRepository) {
        this.grabMenuSyncStatusRepository = grabMenuSyncStatusRepository;
    }

    @Override
    public GrabMenuSyncStatus save(GrabMenuSyncStatus data) {
        return grabMenuSyncStatusRepository.save(data);
    }

    @Override
    public Optional<GrabMenuSyncStatus> findByMerchantId(String merchantId) {
        return grabMenuSyncStatusRepository.findByMerchantId(merchantId);
    }
}

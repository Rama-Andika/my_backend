package com.oxysystem.general.service.transaction;

import com.oxysystem.general.model.tenant.transaction.GrabMenuSyncStatus;

import java.util.Optional;

public interface GrabMenuSyncStatusService {
    GrabMenuSyncStatus save(GrabMenuSyncStatus grabMenuSyncStatus);
    Optional<GrabMenuSyncStatus> findByMerchantId(String merchantId);
}

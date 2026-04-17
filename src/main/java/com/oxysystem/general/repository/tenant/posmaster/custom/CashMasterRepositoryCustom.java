package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.model.tenant.posmaster.CashMaster;

import java.util.Optional;

public interface CashMasterRepositoryCustom {
    Optional<CashMaster> findCashMasterByType(Long locationId, int type);
}

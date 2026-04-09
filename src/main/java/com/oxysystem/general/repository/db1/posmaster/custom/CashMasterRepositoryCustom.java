package com.oxysystem.general.repository.db1.posmaster.custom;

import com.oxysystem.general.model.db1.posmaster.CashMaster;

import java.util.Optional;

public interface CashMasterRepositoryCustom {
    Optional<CashMaster> findCashMasterByType(Long locationId, int type);
}

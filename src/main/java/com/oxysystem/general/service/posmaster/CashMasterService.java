package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.tenant.posmaster.CashMaster;

import java.util.Optional;

public interface CashMasterService {
    Optional<CashMaster> findCashMasterByType(Long locationId, int type);
    CashMaster save(CashMaster cashMaster);
    Optional<Integer> maxCashierNumber();
}

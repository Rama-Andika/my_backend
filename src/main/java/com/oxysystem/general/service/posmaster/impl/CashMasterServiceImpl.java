package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.tenant.posmaster.CashMaster;
import com.oxysystem.general.repository.tenant.posmaster.CashMasterRepository;
import com.oxysystem.general.service.posmaster.CashMasterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CashMasterServiceImpl implements CashMasterService {
    private final CashMasterRepository cashMasterRepository;

    public CashMasterServiceImpl(CashMasterRepository cashMasterRepository) {
        this.cashMasterRepository = cashMasterRepository;
    }

    @Override
    public Optional<CashMaster> findCashMasterByType(Long locationId, int type) {
        return cashMasterRepository.findCashMasterByType(locationId, type);
    }

    @Override
    @Transactional
    public CashMaster save(CashMaster cashMaster) {
        return cashMasterRepository.save(cashMaster);
    }

    @Override
    public Optional<Integer> maxCashierNumber() {
        return cashMasterRepository.maxCashierNumber();
    }
}

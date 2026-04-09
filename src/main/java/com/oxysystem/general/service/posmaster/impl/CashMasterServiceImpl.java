package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.db1.posmaster.CashMaster;
import com.oxysystem.general.repository.db1.posmaster.CashMasterRepository;
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
    @Transactional("db1TransactionManager")
    public CashMaster save(CashMaster cashMaster) {
        return cashMasterRepository.save(cashMaster);
    }

    @Override
    public Optional<Integer> maxCashierNumber() {
        return cashMasterRepository.maxCashierNumber();
    }
}

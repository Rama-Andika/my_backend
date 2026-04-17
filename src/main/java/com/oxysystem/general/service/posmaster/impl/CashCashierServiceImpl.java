package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.tenant.posmaster.CashCashier;
import com.oxysystem.general.repository.tenant.posmaster.CashCashierRepository;
import com.oxysystem.general.service.posmaster.CashCashierService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class CashCashierServiceImpl implements CashCashierService {
    private final CashCashierRepository cashCashierRepository;

    public CashCashierServiceImpl(CashCashierRepository cashCashierRepository) {
        this.cashCashierRepository = cashCashierRepository;
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public CashCashier save(CashCashier cashCashier) {
        return cashCashierRepository.save(cashCashier);
    }

    @Override
    public Optional<CashCashier> findCashCashier(String userId, LocalDate date, Long locationId, Long cashMasterId) {
        return cashCashierRepository.findCashCashier(userId,date,locationId,cashMasterId);
    }
}

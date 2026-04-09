package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.CashCashier;

import java.time.LocalDate;
import java.util.Optional;

public interface CashCashierService {
    CashCashier save(CashCashier cashCashier);
    Optional<CashCashier> findCashCashier(String userId, LocalDate date, Long locationId, Long cashMasterId);
}

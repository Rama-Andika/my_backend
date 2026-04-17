package com.oxysystem.general.service.general;

import com.oxysystem.general.model.tenant.general.Currency;

import java.util.Optional;

public interface CurrencyService {
    Optional<Currency> findIdrCurrency();
}

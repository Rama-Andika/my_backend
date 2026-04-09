package com.oxysystem.general.service.general;

import com.oxysystem.general.model.db1.general.Currency;

import java.util.Optional;

public interface CurrencyService {
    Optional<Currency> findIdrCurrency();
}

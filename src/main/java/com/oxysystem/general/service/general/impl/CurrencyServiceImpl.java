package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.model.db1.general.Currency;
import com.oxysystem.general.repository.db1.general.CurrencyRepository;
import com.oxysystem.general.service.general.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Optional<Currency> findIdrCurrency() {
        return currencyRepository.findIdrCurrency();
    }
}

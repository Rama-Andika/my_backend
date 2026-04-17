package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.model.tenant.general.Currency;
import com.oxysystem.general.repository.tenant.general.CurrencyRepository;
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

package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.bank.view.BankViewDTO;
import com.oxysystem.general.model.tenant.general.Bank;
import com.oxysystem.general.repository.tenant.general.BankRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    public BankServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public Optional<Bank> findById(Long id) {
        return bankRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> getBanks() {
        List<BankViewDTO> banks = bankRepository.getBanks();
        SuccessResponse<Object> response = new SuccessResponse<>("success", banks);
        return ResponseEntity.ok(response);
    }
}

package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.company.view.CompanyViewDTO;
import com.oxysystem.general.repository.tenant.general.CompanyRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Optional<CompanyViewDTO> getCompany() {
        return companyRepository.getCompany();
    }

    @Override
    public ResponseEntity<?> getCompanyForApi() {
        SuccessResponse<?> response = new SuccessResponse<>("success", getCompany());
        return ResponseEntity.ok(response);
    }
}

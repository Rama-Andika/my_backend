package com.oxysystem.general.service.general;

import com.oxysystem.general.dto.general.company.view.CompanyViewDTO;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface CompanyService {
    Optional<CompanyViewDTO> getCompany();
    ResponseEntity<?> getCompanyForApi();
}

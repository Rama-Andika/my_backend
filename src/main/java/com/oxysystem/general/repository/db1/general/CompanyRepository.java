package com.oxysystem.general.repository.db1.general;

import com.oxysystem.general.dto.general.company.view.CompanyViewDTO;
import com.oxysystem.general.model.db1.general.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT new com.oxysystem.general.dto.general.company.view.CompanyViewDTO(CAST(c.companyId as string), " +
            "c.name, c.address, c.phone, c.governmentVat) FROM Company c")
    Optional<CompanyViewDTO> getCompany();
}

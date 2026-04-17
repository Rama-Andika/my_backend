package com.oxysystem.general.repository.tenant.general.custom;

import com.oxysystem.general.dto.general.customer.view.CustomerViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerRepositoryCustom {
    Page<CustomerViewDTO> getCustomers(String name, Pageable pageable);
}

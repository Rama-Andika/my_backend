package com.oxysystem.general.repository.tenant.transaction.custom;

import com.oxysystem.general.dto.transaction.salesTaking.view.SalesTakingPlaygroundViewDTO;
import com.oxysystem.general.dto.transaction.salesTakingDetail.view.SalesTakingDetailQtyReturnDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface SalesTakingRepositoryCustom {
    SalesTakingPlaygroundViewDTO getSalesTakingPlaygroundByNumber(String number);
    Page<SalesTakingPlaygroundViewDTO> getSalesTakingsWithPlayground(String number, String status, Pageable pageable);
    List<SalesTakingDetailQtyReturnDTO> getSalesTakingDetailQtyReturnBySalesId(Long salesId);
}

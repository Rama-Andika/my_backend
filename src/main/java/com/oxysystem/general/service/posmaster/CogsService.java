package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.dto.general.company.view.CompanyViewDTO;
import com.oxysystem.general.dto.posmaster.cogs.view.CogsViewDTO;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.model.tenant.transaction.sales.SalesDetail;

import java.util.List;

public interface CogsService {
    CogsViewDTO cogsByItem(Long itemId);
    List<CogsViewDTO> cogsByItems(List<Long> itemMasterIDs);
    double getLastCogsConsigment(ItemMaster itemMaster, SalesDetail salesDetail, CompanyViewDTO company);
}

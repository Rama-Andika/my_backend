package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.grabMartItemMapping.view.GrabmartItemMappingViewDTO;

import java.util.List;

public interface GrabmartItemMappingRepositoryCustom {
    List<GrabmartItemMappingViewDTO> getGrabmartItemMapping(String golPrice, Long locationId);

    List<GrabmartItemMappingViewDTO> getGrabMartItemMappingByBarcodes(List<String> barcodes);
}

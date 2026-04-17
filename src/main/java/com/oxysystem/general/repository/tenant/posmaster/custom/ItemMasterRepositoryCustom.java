package com.oxysystem.general.repository.tenant.posmaster.custom;

import com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterView2DTO;
import com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemMasterRepositoryCustom {
    Page<ItemMasterViewDTO> getItemMasters(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, Pageable pageable);
    Page<ItemMasterViewDTO> getItemMastersWithPrice(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, String golPrice, Pageable pageable);
    Page<ItemMasterView2DTO> getItemMastersByLocation(String name, String barcode, String code, String imgLink, String golPrice, Long locationId, String category, boolean availability, Pageable pageable);
}

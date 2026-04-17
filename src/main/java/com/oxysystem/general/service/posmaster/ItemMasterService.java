package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterViewDTO;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ItemMasterService {
    ResponseEntity<?> getItemMastersForApi(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, int page, int size);

    ResponseEntity<?> getItemMastersWithPrice(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, Long locationId, int page, int size);

    ResponseEntity<?> getItemMastersByLocation(String name, String barcode, String code, Long locationId, String category, boolean availability, int page, int size);

    Page<ItemMasterViewDTO> getItemMasters(String name, String barcode, String code, Long itemGroupId, Long itemCategoryId, int page, int size);

    Optional<ItemMaster> findItemMasterById(Long id);

    List<ItemMaster> findItemMastersByItemMasterIds(List<Long> ids);

    Optional<ItemMaster> findItemMasterByCodeOrBarcode(String barcode);

    List<ItemMaster> findItemMasterWithPriceTypeByIds(List<Long> itemMasterIds);

    ResponseEntity<?> getItemMastersPlayground();
}

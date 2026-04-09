package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.VendorItem;

import java.util.Optional;

public interface VendorItemService {
    Optional<VendorItem> findVendorItemByItemMasterAndVendor(Long itemMasterId, Long vendorId);
}

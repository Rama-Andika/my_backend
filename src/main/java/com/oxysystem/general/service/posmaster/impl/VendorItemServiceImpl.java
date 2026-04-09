package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.db1.posmaster.VendorItem;
import com.oxysystem.general.repository.db1.posmaster.VendorItemRepository;
import com.oxysystem.general.service.posmaster.VendorItemService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VendorItemServiceImpl implements VendorItemService {
    private final VendorItemRepository vendorItemRepository;

    public VendorItemServiceImpl(VendorItemRepository vendorItemRepository) {
        this.vendorItemRepository = vendorItemRepository;
    }

    @Override
    public Optional<VendorItem> findVendorItemByItemMasterAndVendor(Long itemMasterId, Long vendorId) {
        return vendorItemRepository.findVendorItemByItemMasterAndVendor(itemMasterId, vendorId);
    }
}

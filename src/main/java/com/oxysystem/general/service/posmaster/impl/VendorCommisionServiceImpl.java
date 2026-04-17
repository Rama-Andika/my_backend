package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.tenant.posmaster.VendorCommision;
import com.oxysystem.general.repository.tenant.posmaster.VendorCommisionRepository;
import com.oxysystem.general.service.posmaster.VendorCommisionService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VendorCommisionServiceImpl implements VendorCommisionService {
    private final VendorCommisionRepository vendorCommisionRepository;

    public VendorCommisionServiceImpl(VendorCommisionRepository vendorCommisionRepository) {
        this.vendorCommisionRepository = vendorCommisionRepository;
    }

    @Override
    public Optional<VendorCommision> findVendorCommisionByLocationAndVendor(Long locationId, Long vendorId) {
        return vendorCommisionRepository.findVendorCommisionByLocationAndVendor(locationId, vendorId);
    }
}

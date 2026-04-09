package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.db1.posmaster.VendorCommision;

import java.util.Optional;

public interface VendorCommisionService {
    Optional<VendorCommision> findVendorCommisionByLocationAndVendor(Long locationId, Long vendorId);
}

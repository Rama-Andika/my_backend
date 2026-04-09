package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.VendorCommision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VendorCommisionRepository extends JpaRepository<VendorCommision, Long> {
    @Query(value = "SELECT vc.* FROM vendor_commision vc WHERE vc.location_id = :locationId AND vc.vendor_id = :vendorId LIMIT 1", nativeQuery = true)
    Optional<VendorCommision> findVendorCommisionByLocationAndVendor(@Param("locationId") Long locationId, @Param("vendorId") Long vendorId);
}

package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.VendorItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VendorItemRepository extends JpaRepository<VendorItem, Long> {
    @Query(value = "SELECT vi.* FROM pos_vendor_item vi WHERE item_master_id = :itemMasterId AND vendor_id = :vendorId ORDER BY vi.update_date DESC LIMIT 1", nativeQuery = true)
    Optional<VendorItem> findVendorItemByItemMasterAndVendor(@Param("itemMasterId") Long itemMasterId, @Param("vendorId") Long vendorId);

}

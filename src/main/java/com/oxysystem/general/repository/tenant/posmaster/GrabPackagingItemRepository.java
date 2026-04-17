package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.GrabPackagingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GrabPackagingItemRepository extends JpaRepository<GrabPackagingItem, Long>
{
    @Query(value = "SELECT g.* FROM grab_packaging_item g WHERE g.location_id = :locationId AND g.product = :product LIMIT 1", nativeQuery = true)
    Optional<GrabPackagingItem> findGrabPackagingItemByLocationIdAndProduct(@Param("locationId") Long locationId, @Param("product") String product);

}

package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.PromotionGrabLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PromotionGrabLocationRepository extends JpaRepository<PromotionGrabLocation, Long> {
    @Modifying
    @Query("DELETE FROM PromotionGrabLocation l WHERE l.promotionGrab.id = :promotionId")
    void deletePromotionGrabLocationByPromotionGrabId(@Param("promotionId") Long promotionId);

    @Modifying
    @Query("DELETE FROM PromotionGrabLocation l WHERE l.promotionGrab.id = :promotionId AND l.location.locationId IN :locationIds")
    void deletePromotionGrabLocationByPromotionGrabIdAndLocationIds(@Param("promotionId") Long promotionId, @Param("locationIds") List<Long> locationIds);
}

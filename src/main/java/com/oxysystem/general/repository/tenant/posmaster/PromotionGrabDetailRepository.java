package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.PromotionGrabDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromotionGrabDetailRepository extends JpaRepository<PromotionGrabDetail, Long> {
    @Modifying
    @Query("DELETE FROM PromotionGrabDetail p WHERE p.promotionGrab.id = :promotionGrabId")
    void deletePromotionGrabDetailByPromotionGrabId(@Param("promotionGrabId") Long promotionGrabId);
}

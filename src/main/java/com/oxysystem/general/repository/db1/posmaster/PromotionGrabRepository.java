package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.PromotionGrab;
import com.oxysystem.general.repository.db1.posmaster.custom.PromotionGrabRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PromotionGrabRepository extends JpaRepository<PromotionGrab, Long>, PromotionGrabRepositoryCustom {
    @Query("SELECT MAX(p.counter) FROM PromotionGrab p WHERE p.numberPrefix = :numberPrefix")
    Optional<Integer> getMaxCounterPromotionGrabByNumberPrefix(@Param("numberPrefix") String numberPrefix);
}

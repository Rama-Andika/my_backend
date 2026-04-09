package com.oxysystem.general.repository.db1.transaction;

import com.oxysystem.general.model.db1.transaction.Promotion;
import com.oxysystem.general.repository.db1.transaction.custom.PromotionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long>, PromotionRepositoryCustom {
}

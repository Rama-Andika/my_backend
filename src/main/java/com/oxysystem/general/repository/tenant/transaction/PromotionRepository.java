package com.oxysystem.general.repository.tenant.transaction;

import com.oxysystem.general.model.tenant.transaction.Promotion;
import com.oxysystem.general.repository.tenant.transaction.custom.PromotionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long>, PromotionRepositoryCustom {
}

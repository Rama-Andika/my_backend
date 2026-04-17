package com.oxysystem.general.repository.tenant.transaction;

import com.oxysystem.general.model.tenant.transaction.PromotionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionItemRepository extends JpaRepository<PromotionItem, Long> {
}

package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.PriceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PriceTypeRepository extends JpaRepository<PriceType, Long> {
    @Query(value = "SELECT * FROM pos_price_type WHERE item_master_id = :itemId ORDER BY qty_from, conv_qty LIMIT 1", nativeQuery = true)
    Optional<PriceType> getPriceType(@Param("itemId") Long itemId);
}

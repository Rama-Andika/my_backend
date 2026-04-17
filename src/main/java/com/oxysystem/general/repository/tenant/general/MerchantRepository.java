package com.oxysystem.general.repository.tenant.general;

import com.oxysystem.general.dto.general.merchant.view.MerchantViewDTO;
import com.oxysystem.general.model.tenant.general.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    @Query(value = "SELECT m.* FROM merchant m WHERE LOWER(m.description) = :description AND m.location_id = :locationId LIMIT 1",nativeQuery = true)
    Optional<Merchant> findMerchantByLocationAndDescription(@Param("description") String description,
                                                            @Param("locationId") long locationId);


    @Query("SELECT new com.oxysystem.general.dto.general.merchant.view.MerchantViewDTO(CAST(m.merchantId as string), cast(m.bank.bankId as string), " +
            "cast(m.location.locationId as string), " +
            "cast(m.paymentMethod.paymentMethodId as string), m.description, m.persenExpense, m.postingExpense, m.paymentBy ) FROM Merchant m WHERE m.location.locationId = :locationId")
    List<MerchantViewDTO> getMerchantsByLocationId(@Param("locationId") Long locationId);
}

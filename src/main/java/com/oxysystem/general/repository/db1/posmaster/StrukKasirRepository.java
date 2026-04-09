package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.dto.posmaster.strukKasir.view.StrukKasirViewDTO;
import com.oxysystem.general.model.db1.posmaster.StrukKasir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StrukKasirRepository extends JpaRepository<StrukKasir, Long> {
    @Query(value = "SELECT s.* FROM pos_struk_kasir s WHERE s.grab_merchant_id = :merchantId LIMIT 1", nativeQuery = true)
    Optional<StrukKasir> findByGrabMerchantId(String merchantId);

    @Query(value = "SELECT s.* FROM pos_struk_kasir s WHERE s.grab_food_merchant_id = :merchantId LIMIT 1", nativeQuery = true)
    Optional<StrukKasir> findByGrabFoodMerchantId(String merchantId);

    @Query("SELECT s FROM StrukKasir s WHERE s.grabMerchantId IS NOT NULL AND s.grabMerchantId != '' ")
    List<StrukKasir> findStrukKasirGrabMerchantIDNotNull();

    @Query("SELECT s FROM StrukKasir s WHERE s.grabFoodMerchantId IS NOT NULL AND s.grabFoodMerchantId != '' ")
    List<StrukKasir> findStrukKasirGrabFoodMerchantIDNotNull();

    @Query(value = "SELECT s.* FROM pos_struk_kasir s WHERE s.location_id = :locationId LIMIT 1", nativeQuery = true)
    Optional<StrukKasir> findStrukKasirByLocationId(@Param("locationId") Long locationId);

    @Query(value = "SELECT s.* FROM pos_struk_kasir s WHERE s.location_id IN :locationIds", nativeQuery = true)
    List<StrukKasir> findStrukKasirByLocationIds(@Param("locationIds") List<Long> locationIds);

    @Query("SELECT new com.oxysystem.general.dto.posmaster.strukKasir.view.StrukKasirViewDTO(CAST(s.strukKasirId as string), " +
            "s.header, s.footer, s.footer1, s.footer2) FROM StrukKasir s WHERE s.locationId = :locationId")
    Optional<StrukKasirViewDTO> findByLocationId(@Param("locationId") Long locationId);
}

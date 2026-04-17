package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.dto.general.strukKasir.data.StrukKasirDTO;
import com.oxysystem.general.model.tenant.posmaster.StrukKasir;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StrukKasirService {

    Optional<StrukKasir> findByGrabMerchantId(@Param("merchantId") String merchantId);
    Optional<StrukKasir> findByGrabFoodMerchantId(@Param("merchantId") String merchantId);
    List<StrukKasir> findStrukKasirGrabMerchantIDNotNull();
    List<StrukKasir> findStrukKasirGrabFoodMerchantIDNotNull();
    Optional<StrukKasir> findStrukKasirByLocationId(Long locationId);
    List<StrukKasir> findStrukKasirByLocationIds(List<Long> locationIds);
    ResponseEntity<?> save(StrukKasirDTO body);
    ResponseEntity<?> getStrukKasirByLocationId(Long locationId);
}

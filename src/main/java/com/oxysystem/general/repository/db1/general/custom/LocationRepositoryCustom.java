package com.oxysystem.general.repository.db1.general.custom;

import com.oxysystem.general.dto.general.location.view.GrabPackagingItemLocationViewDTO;
import com.oxysystem.general.dto.general.location.view.LocationViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface LocationRepositoryCustom {
    Page<LocationViewDTO> getLocations(String name, String grabMerchantId, String grabFoodMerchantId, Pageable pageable);
    Page<GrabPackagingItemLocationViewDTO> getPackagingItemLocations(String name, String product, Pageable pageable);
}

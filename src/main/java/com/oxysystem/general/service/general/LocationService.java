package com.oxysystem.general.service.general;

import com.oxysystem.general.dto.general.location.view.LocationGrabForSelectDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.model.db1.general.Location;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    Optional<Location> findById(Long id);
    List<Location> findAllByIDs(List<Long> ids);
    ResponseEntity<?> getLocationsGrabForSelectForApi(String product);
    ResponseEntity<?> getLocations(String name, String grabMerchantId, String grabFoodMerchantId, int page, int size);
    ResponseEntity<?> getPackagingItemLocations(String name, String product, int page, int size);
    List<LocationGrabForSelectDTO> getLocationsGrabForSelect(String product);
}

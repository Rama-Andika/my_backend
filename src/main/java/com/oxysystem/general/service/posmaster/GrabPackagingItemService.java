package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.dto.posmaster.grabPackaging.data.GrabPackagingItemDTO;
import com.oxysystem.general.model.db1.posmaster.GrabPackagingItem;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface GrabPackagingItemService {
    ResponseEntity<Object> save(GrabPackagingItemDTO body);
    ResponseEntity<Object> update(GrabPackagingItemDTO body, Long id);
    Optional<GrabPackagingItem> findGrabPackagingItemByLocationIdAndProduct(Long locationId, String product);
}

package com.oxysystem.general.service.posmaster;


import com.oxysystem.general.dto.posmaster.grabMartItemMapping.data.GrabMartItemByItemMasterDTO;
import com.oxysystem.general.dto.posmaster.grabMartItemMapping.data.UploadItemMasterGrabMartDTO;
import com.oxysystem.general.dto.posmaster.grabMartItemMapping.view.GrabmartItemMappingViewDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

public interface GrabmartItemMappingService {
    List<GrabmartItemMappingViewDTO> getListGrabmartItemMapping(String golPrice, Long locationId);
    ResponseEntity<?> saveGrabmartItem(GrabMartItemByItemMasterDTO grabMartItemByItemMasterDTO);
    ResponseEntity<?> findGrabmartItemMappingByItemMasterId(Long id);
    ResponseEntity<Object> uploadItemMasterGrabMart(List<UploadItemMasterGrabMartDTO> data, Long locationId);
}

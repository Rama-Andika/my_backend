package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.dto.posmaster.grabMartGroup.data.GrabMartItemGroupDTO;
import com.oxysystem.general.model.db1.posmaster.GrabmartItemGroup;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface GrabmartItemGroupService {
    ResponseEntity<?> update(Long id, GrabMartItemGroupDTO body);
    ResponseEntity<?> getGrabMartItemGroups(String name);
    ResponseEntity<?> syncCategories(String token, String countryCode);
    ResponseEntity<?> getGrabmartItemGroupForSelectForApi();
    Optional<GrabmartItemGroup> findById(Long id);
    List<GrabmartItemGroup> findAllGrabMartItemGroupByIds(List<Long> ids);
    ResponseEntity<?> toCsv(String name) throws IOException;
}

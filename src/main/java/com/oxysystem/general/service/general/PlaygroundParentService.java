package com.oxysystem.general.service.general;

import com.oxysystem.general.dto.general.parent.data.ParentDTO;
import org.springframework.http.ResponseEntity;

public interface PlaygroundParentService {
    ResponseEntity<?> save(ParentDTO body);
}

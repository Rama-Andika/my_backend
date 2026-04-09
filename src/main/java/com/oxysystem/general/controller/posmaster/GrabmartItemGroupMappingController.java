package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.service.posmaster.GrabmartItemMappingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GrabmartItemGroupMappingController {
    private final GrabmartItemMappingService grabmartItemGroupMappingService;

    public GrabmartItemGroupMappingController(GrabmartItemMappingService grabmartItemMappingService) {
        this.grabmartItemGroupMappingService = grabmartItemMappingService;
    }

}

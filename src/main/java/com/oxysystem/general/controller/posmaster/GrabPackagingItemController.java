package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.dto.posmaster.grabPackaging.data.GrabPackagingItemDTO;
import com.oxysystem.general.model.db1.posmaster.GrabPackagingItem;
import com.oxysystem.general.service.posmaster.GrabPackagingItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class GrabPackagingItemController {
    private final GrabPackagingItemService grabPackagingItemService;

    public GrabPackagingItemController(GrabPackagingItemService grabPackagingItemService) {
        this.grabPackagingItemService = grabPackagingItemService;
    }

    @PostMapping("/grab-packaging-item")
    public ResponseEntity<Object> saveGrabPackagingItem(@RequestBody GrabPackagingItemDTO body) {
        return grabPackagingItemService.save(body);
    }

    @PutMapping("/grab-packaging-item/{id}")
    public ResponseEntity<Object> saveGrabPackagingItem(@RequestBody GrabPackagingItemDTO body, @PathVariable Long id) {
        return grabPackagingItemService.update(body, id);
    }
}

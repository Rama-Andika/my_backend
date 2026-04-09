package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.service.posmaster.ItemGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ItemGroupController {
    private final ItemGroupService itemGroupService;

    public ItemGroupController(ItemGroupService itemGroupService) {
        this.itemGroupService = itemGroupService;
    }

    @GetMapping("/item-group/select")
    public ResponseEntity<?> getItemGroupForSelect(){
        return itemGroupService.getItemGroupForSelect();
    }
}

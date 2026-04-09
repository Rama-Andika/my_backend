package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.dto.posmaster.grabFoodGroup.data.GrabFoodItemGroupDTO;
import com.oxysystem.general.dto.posmaster.grabMartGroup.data.GrabMartItemGroupDTO;
import com.oxysystem.general.service.posmaster.GrabfoodItemGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class GrabfoodItemGroupController {
    private final GrabfoodItemGroupService grabfoodItemGroupService;

    public GrabfoodItemGroupController(GrabfoodItemGroupService grabfoodItemGroupService) {
        this.grabfoodItemGroupService = grabfoodItemGroupService;
    }

    @PutMapping("/grabfood/categories/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody GrabFoodItemGroupDTO body){
        return grabfoodItemGroupService.update(id, body);
    }

    @GetMapping("/grabfood/categories")
    public ResponseEntity<?> getGrabMartItemGroups(@RequestParam(required = false) String name){
        return grabfoodItemGroupService.getGrabFoodItemGroups(name);
    }

    @GetMapping("/grabfood-item-group/select")
    public ResponseEntity<?> getGrabmartItemGroupForSelect(){
        return grabfoodItemGroupService.getGrabfoodItemGroupForSelectForApi();
    }
}

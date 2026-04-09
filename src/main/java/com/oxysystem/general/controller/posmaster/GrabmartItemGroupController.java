package com.oxysystem.general.controller.posmaster;

import com.oxysystem.general.dto.posmaster.grabMartGroup.data.GrabMartItemGroupDTO;
import com.oxysystem.general.service.posmaster.GrabmartItemGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class GrabmartItemGroupController {
    private final GrabmartItemGroupService grabmartItemGroupService;

    public GrabmartItemGroupController(GrabmartItemGroupService grabmartItemGroupService) {
        this.grabmartItemGroupService = grabmartItemGroupService;
    }

    @PutMapping("/grabmart/categories/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody GrabMartItemGroupDTO body){
        return grabmartItemGroupService.update(id, body);
    }

    @GetMapping("/grabmart/categories")
    public ResponseEntity<?> getGrabMartItemGroups(@RequestParam(required = false) String name){
        return grabmartItemGroupService.getGrabMartItemGroups(name);
    }

    @GetMapping("/sync-categories")
    public ResponseEntity<?> syncCategories(@RequestHeader("Authorization") String bearer, @RequestParam String countryCode){
        String token = bearer.replace("Bearer","").trim();
        return grabmartItemGroupService.syncCategories(token, countryCode);
    }

    @GetMapping("/grabmart-item-group/select")
    public ResponseEntity<?> getGrabmartItemGroupForSelect(){
        return grabmartItemGroupService.getGrabmartItemGroupForSelectForApi();
    }

    @GetMapping("/export/grabmart-category")
    public ResponseEntity<?> toCsv(String name) throws IOException {
        return grabmartItemGroupService.toCsv(name);
    }
}

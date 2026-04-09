package com.oxysystem.general.controller.grab.client.grabMart;

import com.oxysystem.general.dto.grab.data.BatchUpdateMenuRequestDTO;
import com.oxysystem.general.dto.grab.data.UpdateMenuNotificationRequestDTO;
import com.oxysystem.general.dto.grab.data.UpdateMenuRecordRequestDTO;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartMenuSyncServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/grabmart")
public class GrabMartMenuSyncController {
    private final GrabMartMenuSyncServiceImpl grabMartMenuSyncService;

    public GrabMartMenuSyncController(GrabMartMenuSyncServiceImpl grabMartMenuSyncService) {
        this.grabMartMenuSyncService = grabMartMenuSyncService;
    }

    @PostMapping("/merchant/menu/notification")
    public ResponseEntity<?> UpdateMenuNotification(@Valid @RequestBody UpdateMenuNotificationRequestDTO request) {
        String token = request.getToken();
        return grabMartMenuSyncService.UpdateMenuNotification(token, request);
    }

    @GetMapping("/menu/categories")
    public ResponseEntity<?> listMartCategories(@RequestHeader("Authorization") String bearer, @RequestParam() String countryCode){
        String token = bearer.replace("Bearer","").trim();

        return grabMartMenuSyncService.listMartCategories(token,countryCode);
    }

    @PutMapping("/menu")
    public ResponseEntity<?> updateMenuRecord(@RequestHeader("Authorization") String bearer, @Valid @RequestBody UpdateMenuRecordRequestDTO updateMenuRecordRequestDTO){
        String token = bearer.replace("Bearer","").trim();
        return grabMartMenuSyncService.updateMenuRecord(token, updateMenuRecordRequestDTO);
    }

    @PutMapping("/batch/menu")
    public ResponseEntity<?> batchUpdateMenuRecord(@RequestHeader("Authorization") String bearer, @Valid @RequestBody BatchUpdateMenuRequestDTO batchUpdateMenuRequestDTO){
        String token = bearer.replace("Bearer","").trim();
        return grabMartMenuSyncService.batchUpdateMenu(token, batchUpdateMenuRequestDTO);
    }
}

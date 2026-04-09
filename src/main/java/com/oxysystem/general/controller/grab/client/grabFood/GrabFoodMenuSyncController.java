package com.oxysystem.general.controller.grab.client.grabFood;

import com.oxysystem.general.dto.grab.data.BatchUpdateMenuRequestDTO;
import com.oxysystem.general.dto.grab.data.UpdateMenuNotificationRequestDTO;
import com.oxysystem.general.dto.grab.data.UpdateMenuRecordRequestDTO;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodMenuSyncServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/grabfood")
public class GrabFoodMenuSyncController {
    private final GrabFoodMenuSyncServiceImpl grabFoodMenuSyncService;

    public GrabFoodMenuSyncController(GrabFoodMenuSyncServiceImpl grabFoodMenuSyncService) {
        this.grabFoodMenuSyncService = grabFoodMenuSyncService;
    }

    @PostMapping("/merchant/menu/notification")
    public ResponseEntity<?> UpdateMenuNotification(@Valid @RequestBody UpdateMenuNotificationRequestDTO request) {
        String token = request.getToken();

        return grabFoodMenuSyncService.UpdateMenuNotification(token, request);
    }

    @PutMapping("/menu")
    public ResponseEntity<?> updateMenuRecord(@RequestHeader("Authorization") String bearer, @Valid @RequestBody UpdateMenuRecordRequestDTO updateMenuRecordRequestDTO){
        String token = bearer.replace("Bearer","").trim();
        return grabFoodMenuSyncService.updateMenuRecord(token, updateMenuRecordRequestDTO);
    }

    @PutMapping("/batch/menu")
    public ResponseEntity<?> batchUpdateMenuRecord(@RequestHeader("Authorization") String bearer, @Valid @RequestBody BatchUpdateMenuRequestDTO batchUpdateMenuRequestDTO){
        String token = bearer.replace("Bearer","").trim();
        return grabFoodMenuSyncService.batchUpdateMenu(token, batchUpdateMenuRequestDTO);
    }
}

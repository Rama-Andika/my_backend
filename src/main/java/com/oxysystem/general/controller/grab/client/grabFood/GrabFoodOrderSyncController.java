package com.oxysystem.general.controller.grab.client.grabFood;

import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOrderSyncServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/grabfood")
public class GrabFoodOrderSyncController {
    private final GrabFoodOrderSyncServiceImpl grabFoodOrderSyncService;

    public GrabFoodOrderSyncController(GrabFoodOrderSyncServiceImpl grabFoodOrderSyncService) {
        this.grabFoodOrderSyncService = grabFoodOrderSyncService;
    }

    @GetMapping("/orders")
    public ResponseEntity<?> listOrder(@RequestHeader("Authorization") String bearerToken,
                                       @RequestParam String merchantID,
                                       @RequestParam(required = false) String date,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) List<String> orderIDs){
        String token = bearerToken.replace("Bearer","").trim();
        return grabFoodOrderSyncService.listOrder(token, merchantID, date, page, orderIDs);
    }

    @GetMapping("/orders-v2")
    public ResponseEntity<?> listOrderMappingToListOrderDTO( @RequestParam Long locationId,
                                                             @RequestParam(required = false) String date,
                                                             @RequestParam(required = false) Integer page,
                                                             @RequestParam(required = false) List<String> orderIDs){
        return grabFoodOrderSyncService.listOrderMappingToListOrderDTO(locationId, date, page, orderIDs);
    }
}

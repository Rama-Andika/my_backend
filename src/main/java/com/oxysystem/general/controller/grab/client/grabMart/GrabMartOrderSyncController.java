package com.oxysystem.general.controller.grab.client.grabMart;

import com.oxysystem.general.service.grab.client.grabMart.GrabMartOrderSyncServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/grabmart")
public class GrabMartOrderSyncController {
    private final GrabMartOrderSyncServiceImpl grabMartOrderSyncService;

    public GrabMartOrderSyncController(GrabMartOrderSyncServiceImpl grabMartOrderSyncService) {
        this.grabMartOrderSyncService = grabMartOrderSyncService;
    }

    @GetMapping("/orders")
    public ResponseEntity<?> listOrder(@RequestHeader("Authorization") String bearerToken,
                                       @RequestParam String merchantID,
                                       @RequestParam(required = false) String date,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) List<String> orderIDs){
        String token = bearerToken.replace("Bearer","").trim();
        return grabMartOrderSyncService.listOrder(token, merchantID, date, page, orderIDs);
    }

    @GetMapping("/orders-v2")
    public ResponseEntity<?> listOrderMappingToListOrderDTO( @RequestParam Long locationId,
                                       @RequestParam(required = false) String date,
                                       @RequestParam(required = false) Integer page,
                                       @RequestParam(required = false) List<String> orderIDs){
        return grabMartOrderSyncService.listOrderMappingToListOrderDTO(locationId, date, page, orderIDs);
    }
}

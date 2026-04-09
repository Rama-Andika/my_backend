package com.oxysystem.general.controller.grab.partner.grabFood;

import com.oxysystem.general.dto.grab.data.PushOrderStateRequestDTO;
import com.oxysystem.general.dto.grab.data.SubmitOrderRequestDTO;
import com.oxysystem.general.service.grab.partner.grabFood.GrabFoodOrderSyncPartnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/partner/grabfood")
public class GrabFoodOrderSyncPartnerController {
    private final GrabFoodOrderSyncPartnerServiceImpl grabFoodOrderSyncPartnerService;

    public GrabFoodOrderSyncPartnerController(GrabFoodOrderSyncPartnerServiceImpl grabFoodOrderSyncPartnerService) {
        this.grabFoodOrderSyncPartnerService = grabFoodOrderSyncPartnerService;
    }

    @PostMapping("/orders")
    public ResponseEntity<?> submitOrder(@Valid @RequestBody SubmitOrderRequestDTO submitOrderRequestDTO){
        return grabFoodOrderSyncPartnerService.submitOrderForApi(submitOrderRequestDTO);
    }

    @PutMapping("/order/state")
    public ResponseEntity<?> pushOrderState(@Valid @RequestBody PushOrderStateRequestDTO pushOrderStateRequestDTO){
        return grabFoodOrderSyncPartnerService.pushOrderStateForApi(pushOrderStateRequestDTO);
    }

    @PostMapping("/order/manual-upload")
    public ResponseEntity<?> manualUploadPerSales(String number, Long locationId){
        return grabFoodOrderSyncPartnerService.manualUploadPerSalesForApi(number, locationId);
    }
}

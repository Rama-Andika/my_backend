package com.oxysystem.general.controller.grab.partner.grabMart;

import com.oxysystem.general.dto.grab.data.PushOrderStateRequestDTO;
import com.oxysystem.general.dto.grab.data.SubmitOrderRequestDTO;
import com.oxysystem.general.service.grab.partner.grabMart.GrabMartOrderSyncPartnerServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/partner/grabmart")
public class GrabMartOrderSyncPartnerController {
    private final GrabMartOrderSyncPartnerServiceImpl grabMartOrderSyncPartnerService;

    public GrabMartOrderSyncPartnerController(GrabMartOrderSyncPartnerServiceImpl grabMartOrderSyncPartnerService) {
        this.grabMartOrderSyncPartnerService = grabMartOrderSyncPartnerService;
    }

    @PostMapping("/orders")
    public ResponseEntity<?> submitOrder(@Valid @RequestBody SubmitOrderRequestDTO submitOrderRequestDTO){
        return grabMartOrderSyncPartnerService.submitOrderForApi(submitOrderRequestDTO);
    }

    @PutMapping("/order/state")
    public ResponseEntity<?> pushOrderState(@Valid @RequestBody PushOrderStateRequestDTO pushOrderStateRequestDTO){
        return grabMartOrderSyncPartnerService.pushOrderStateForApi(pushOrderStateRequestDTO);
    }

    @PostMapping("/order/manual-upload")
    public ResponseEntity<?> manualUploadPerSales(String number, Long locationId){
        return grabMartOrderSyncPartnerService.manualUploadPerSalesForApi(number, locationId);
    }
}

package com.oxysystem.general.controller.grab.client.grabMart;

import com.oxysystem.general.service.grab.client.grabMart.GrabMartCampaignServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/grabmart")
public class GrabMartCampaignController {
    private final GrabMartCampaignServiceImpl grabMartCampaignService;

    public GrabMartCampaignController(GrabMartCampaignServiceImpl grabMartCampaignService) {
        this.grabMartCampaignService = grabMartCampaignService;
    }

    @GetMapping("/campaigns")
    public ResponseEntity<?> listCampaign(String merchantID){
        return grabMartCampaignService.listCampaign(merchantID);
    }
}

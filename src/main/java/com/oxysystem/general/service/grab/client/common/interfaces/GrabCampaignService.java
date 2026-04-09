package com.oxysystem.general.service.grab.client.common.interfaces;

import org.springframework.http.ResponseEntity;

public interface GrabCampaignService {
    ResponseEntity<?> listCampaign(String merchantID);
    void deleteCampaign(String campaignId, String token);
}

package com.oxysystem.general.service.grab.client.grabFood;

import com.oxysystem.general.service.grab.client.common.GrabCampaignCommon;
import com.oxysystem.general.service.grab.client.common.interfaces.GrabCampaignService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GrabFoodCampaignServiceImpl implements GrabCampaignService {
    private final GrabFoodOAuthServiceImpl grabfoodOAuthService;
    private final GrabCampaignCommon grabCampaignCommon;
    private final WebClient grabFoodClient;

    public GrabFoodCampaignServiceImpl(GrabFoodOAuthServiceImpl grabfoodOAuthService, GrabCampaignCommon grabCampaignCommon, WebClient grabFoodClient) {
        this.grabfoodOAuthService = grabfoodOAuthService;
        this.grabCampaignCommon = grabCampaignCommon;
        this.grabFoodClient = grabFoodClient;
    }

    @Override
    public ResponseEntity<?> listCampaign(String merchantID) {
        String token = grabfoodOAuthService.getGrabToken();
        return grabCampaignCommon.listCampaign(grabFoodClient, merchantID, token);
    }

    @Override
    public void deleteCampaign(String campaignId, String token) {
        if(token == null || token.isEmpty()){
            token = grabfoodOAuthService.getGrabToken();
        }
        grabCampaignCommon.deleteCampaign(grabFoodClient, campaignId, token);
    }
}

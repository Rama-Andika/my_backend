package com.oxysystem.general.service.grab.client.grabMart;

import com.oxysystem.general.service.grab.client.common.GrabCampaignCommon;
import com.oxysystem.general.service.grab.client.common.interfaces.GrabCampaignService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GrabMartCampaignServiceImpl implements GrabCampaignService {
    private final WebClient grabMartClient;
    private final GrabMartOAuthServiceImpl grabMartOAuthService;
    private final GrabCampaignCommon grabCampaignCommon;

    public GrabMartCampaignServiceImpl(WebClient grabMartClient, GrabMartOAuthServiceImpl grabMartOAuthService, GrabCampaignCommon grabCampaignCommon) {
        this.grabMartClient = grabMartClient;
        this.grabMartOAuthService = grabMartOAuthService;
        this.grabCampaignCommon = grabCampaignCommon;
    }

    @Override
    public ResponseEntity<?> listCampaign(String merchantID) {
        String token = grabMartOAuthService.getGrabToken();
        return grabCampaignCommon.listCampaign(grabMartClient, merchantID, token);
    }

    @Override
    public void deleteCampaign(String campaignId, String token) {
        if(token == null || token.isEmpty()){
            token = grabMartOAuthService.getGrabToken();
        }
        grabCampaignCommon.deleteCampaign(grabMartClient, campaignId, token);
    }
}

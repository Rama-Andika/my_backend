package com.oxysystem.general.service.grab.partner.grabMart;

import com.oxysystem.general.dto.grab.data.PushIntegrationStatusRequestDTO;
import com.oxysystem.general.dto.grab.view.GrabMenuDTO;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.service.grab.partner.common.interfaces.GrabOnboardingPartnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public class GrabMartOnboardingPartnerServiceImpl implements GrabOnboardingPartnerService {
    private final static Logger LOGGER = LoggerFactory.getLogger(GrabMartOnboardingPartnerServiceImpl.class);


    @Override
    public ResponseEntity<?> pushIntegrationStatus(PushIntegrationStatusRequestDTO pushIntegrationStatusRequestDTO) {
        LOGGER.info("Integration status is {}",pushIntegrationStatusRequestDTO.getIntegrationStatus());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    public ResponseEntity<?> pushGrabMenu(GrabMenuDTO martMenu) {
        if(martMenu.getMerchantID() == null || (martMenu.getMerchantID().isEmpty())) throw new ResourceNotFoundException("missing merchant ID parameter!");
        if(martMenu.getPartnerMerchantID() == null || martMenu.getPartnerMerchantID().isEmpty()) throw new ResourceNotFoundException("missing partner merchant ID parameter!");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

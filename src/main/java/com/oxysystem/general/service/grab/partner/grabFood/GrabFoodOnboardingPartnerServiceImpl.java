package com.oxysystem.general.service.grab.partner.grabFood;

import com.oxysystem.general.dto.grab.data.PushIntegrationStatusRequestDTO;
import com.oxysystem.general.dto.grab.view.GrabMenuDTO;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.service.grab.partner.common.interfaces.GrabOnboardingPartnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GrabFoodOnboardingPartnerServiceImpl implements GrabOnboardingPartnerService {
    @Override
    public ResponseEntity<?> pushIntegrationStatus(PushIntegrationStatusRequestDTO pushIntegrationStatusRequestDTO) {
        log.info("Integration status grab food is {}",pushIntegrationStatusRequestDTO.getIntegrationStatus());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    public ResponseEntity<?> pushGrabMenu(GrabMenuDTO martMenu) {
        if(martMenu.getMerchantID() == null || (martMenu.getMerchantID().isEmpty())) throw new ResourceNotFoundException("missing merchant ID parameter!");
        if(martMenu.getPartnerMerchantID() == null || martMenu.getPartnerMerchantID().isEmpty()) throw new ResourceNotFoundException("missing partner merchant ID parameter!");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}

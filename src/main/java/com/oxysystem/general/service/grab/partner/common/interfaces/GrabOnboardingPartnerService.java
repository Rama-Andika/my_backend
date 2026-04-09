package com.oxysystem.general.service.grab.partner.common.interfaces;

import com.oxysystem.general.dto.grab.data.PushIntegrationStatusRequestDTO;
import com.oxysystem.general.dto.grab.view.GrabMenuDTO;
import org.springframework.http.ResponseEntity;

public interface GrabOnboardingPartnerService {
    ResponseEntity<?> pushIntegrationStatus(PushIntegrationStatusRequestDTO pushIntegrationStatusRequestDTO);
    ResponseEntity<?> pushGrabMenu(GrabMenuDTO martMenu);
}

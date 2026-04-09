package com.oxysystem.general.service.grab.partner.common.interfaces;

import com.oxysystem.general.dto.grab.data.MenuSyncStateRequestDTO;
import org.springframework.http.ResponseEntity;

public interface GrabMenuSyncPartnerService {
    ResponseEntity<?> getMenu(String merchantID, String partnerMerchantID);
    ResponseEntity<?> menuSyncState(MenuSyncStateRequestDTO menuSyncStateRequestDTO);
}

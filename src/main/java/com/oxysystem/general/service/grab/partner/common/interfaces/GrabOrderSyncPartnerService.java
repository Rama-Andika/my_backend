package com.oxysystem.general.service.grab.partner.common.interfaces;

import com.oxysystem.general.dto.grab.data.ListOrderResponseDTO;
import com.oxysystem.general.dto.grab.data.PushOrderStateRequestDTO;
import com.oxysystem.general.dto.grab.data.SubmitOrderRequestDTO;
import org.springframework.http.ResponseEntity;

public interface GrabOrderSyncPartnerService {
    ResponseEntity<?> submitOrderForApi(SubmitOrderRequestDTO submitOrderRequestDTO);
    ResponseEntity<?> pushOrderStateForApi(PushOrderStateRequestDTO pushOrderStateRequestDTO);
    ResponseEntity<?> manualUploadPerSalesForApi(String number, Long locationId);
    void automaticUploadSales(ListOrderResponseDTO.Order order);
}

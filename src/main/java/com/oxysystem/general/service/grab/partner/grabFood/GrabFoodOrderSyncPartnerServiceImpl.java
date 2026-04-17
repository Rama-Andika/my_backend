package com.oxysystem.general.service.grab.partner.grabFood;

import com.oxysystem.general.dto.grab.data.ListOrderResponseDTO;
import com.oxysystem.general.dto.grab.data.PushOrderStateRequestDTO;
import com.oxysystem.general.dto.grab.data.SubmitOrderRequestDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodMenuSyncServiceImpl;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOAuthServiceImpl;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOrderSyncServiceImpl;
import com.oxysystem.general.service.grab.partner.common.GrabOrderSyncOrderPartnerCommon;
import com.oxysystem.general.service.grab.partner.common.interfaces.GrabOrderSyncPartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrabFoodOrderSyncPartnerServiceImpl implements GrabOrderSyncPartnerService {
    private final GrabFoodMenuSyncServiceImpl grabFoodMenuSyncService;
    private final GrabOrderSyncOrderPartnerCommon grabOrderSyncOrderPartnerCommon;
    private final GrabFoodOAuthServiceImpl grabFoodOAuthService;
    private final GrabFoodOrderSyncServiceImpl grabFoodOrderSyncService;

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<?> submitOrderForApi(SubmitOrderRequestDTO submitOrderRequestDTO) {
        grabOrderSyncOrderPartnerCommon.submitOrder(submitOrderRequestDTO, Product.GRAB_FOOD.name());

        grabOrderSyncOrderPartnerCommon.batchUpdateMenu(grabFoodMenuSyncService, submitOrderRequestDTO.getItems(), submitOrderRequestDTO.getMerchantID());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<?> pushOrderStateForApi(PushOrderStateRequestDTO pushOrderStateRequestDTO) {
        grabOrderSyncOrderPartnerCommon.pushOrderState(pushOrderStateRequestDTO, null, Product.GRAB_FOOD.name());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<?> manualUploadPerSalesForApi(String number, Long locationId) {
        return grabOrderSyncOrderPartnerCommon.manualUploadPerSalesForApi(grabFoodOAuthService, grabFoodOrderSyncService, number, locationId, Product.GRAB_FOOD.name());
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public void automaticUploadSales(ListOrderResponseDTO.Order order) {
        grabOrderSyncOrderPartnerCommon.automaticUploadSales(order, Product.GRAB_FOOD.name());
    }
}

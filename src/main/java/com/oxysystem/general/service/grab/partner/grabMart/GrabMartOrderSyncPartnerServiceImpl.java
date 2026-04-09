package com.oxysystem.general.service.grab.partner.grabMart;

import com.oxysystem.general.dto.grab.data.*;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartMenuSyncServiceImpl;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartOAuthServiceImpl;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartOrderSyncServiceImpl;
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
public class GrabMartOrderSyncPartnerServiceImpl implements GrabOrderSyncPartnerService {
    private final GrabMartMenuSyncServiceImpl grabMartMenuSyncService;
    private final GrabOrderSyncOrderPartnerCommon grabOrderSyncOrderPartnerCommon;
    private final GrabMartOAuthServiceImpl grabMartOAuthService;
    private final GrabMartOrderSyncServiceImpl grabMartOrderSyncService;

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> submitOrderForApi(SubmitOrderRequestDTO submitOrderRequestDTO) {
        grabOrderSyncOrderPartnerCommon.submitOrder(submitOrderRequestDTO, Product.GRAB_MART.name());

        grabOrderSyncOrderPartnerCommon.batchUpdateMenu(grabMartMenuSyncService, submitOrderRequestDTO.getItems(), submitOrderRequestDTO.getMerchantID());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> pushOrderStateForApi(PushOrderStateRequestDTO pushOrderStateRequestDTO) {
        grabOrderSyncOrderPartnerCommon.pushOrderState(pushOrderStateRequestDTO, null, Product.GRAB_MART.name());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> manualUploadPerSalesForApi(String number, Long locationId) {
        return grabOrderSyncOrderPartnerCommon.manualUploadPerSalesForApi(grabMartOAuthService, grabMartOrderSyncService, number, locationId, Product.GRAB_MART.name());
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public void automaticUploadSales(ListOrderResponseDTO.Order order) {
           grabOrderSyncOrderPartnerCommon.automaticUploadSales(order, Product.GRAB_MART.name());
    }
}

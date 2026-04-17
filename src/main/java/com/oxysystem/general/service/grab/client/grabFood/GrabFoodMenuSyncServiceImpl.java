package com.oxysystem.general.service.grab.client.grabFood;

import com.oxysystem.general.dto.grab.data.*;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.exception.GrabException;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.service.grab.client.common.GrabMenuSyncCommon;
import com.oxysystem.general.service.grab.client.common.interfaces.GrabMenuSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class GrabFoodMenuSyncServiceImpl implements GrabMenuSyncService {
    private final WebClient grabFoodClient;
    private final GrabMenuSyncCommon grabMenuSyncCommon;
    private final GrabFoodOAuthServiceImpl grabFoodOAuthService;

    public GrabFoodMenuSyncServiceImpl(WebClient grabFoodClient, GrabMenuSyncCommon grabMenuSyncCommon, GrabFoodOAuthServiceImpl grabFoodOAuthService) {
        this.grabFoodClient = grabFoodClient;
        this.grabMenuSyncCommon = grabMenuSyncCommon;
        this.grabFoodOAuthService = grabFoodOAuthService;
    }

    @Override
    public ResponseEntity<?> UpdateMenuNotification(String token, UpdateMenuNotificationRequestDTO request) {
        return grabMenuSyncCommon.UpdateMenuNotification(grabFoodClient, token, request);
    }

    @Override
    public Mono<Void> updateMenuNotifyReactive(String token, UpdateMenuNotificationRequestDTO request) {
        return grabMenuSyncCommon.updateMenuNotifyReactive(grabFoodClient, token, request);
    }

    @Override
    public ResponseEntity<?> updateMenuRecord(String token, UpdateMenuRecordRequestDTO updateMenuRecordRequestDTO) {
        return grabMenuSyncCommon.updateMenuRecord(grabFoodClient, token, updateMenuRecordRequestDTO);
    }

    @Override
    public ResponseEntity<?> batchUpdateMenu(String token, BatchUpdateMenuRequestDTO batchUpdateMenuRequestDTO) {
        return grabMenuSyncCommon.batchUpdateMenu(grabFoodClient, token, batchUpdateMenuRequestDTO);
    }

    @Recover
    public void recover(GrabException ex, String token, UpdateMenuNotificationRequestDTO request) {
        log.error("Retry exhausted: WebClientRequestException for merchant ID {}: {}", request.getMerchantID(), ex.getMessage(), ex);
    }

    @Recover
    public void recover(WebClientResponseException ex, String token, UpdateMenuNotificationRequestDTO request) {
        log.error("Retry exhausted: WebClientRequestException for merchant ID {}: {}", request.getMerchantID(), ex.getMessage(), ex);
    }

    @Recover
    public void recover(WebClientRequestException ex, UpdateMenuNotificationRequestDTO request) {
        log.error("Retry exhausted: WebClientRequestException for merchant ID {}: {}", request.getMerchantID(), ex.getMessage(), ex);
    }

    @Override
    @Retryable(
            value = {WebClientRequestException.class, WebClientResponseException.class, GrabException.class},
            maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(
                    delayExpression = "${retry.delay}",
                    maxDelayExpression = "${retry.maxDelay}",
                    multiplierExpression = "${retry.multiplier}"
            )
    )
    public void batchUpdateMenu(BatchUpdateMenuRequestDTO batchUpdateMenuRequestDTO) {
        String token = grabFoodOAuthService.getGrabToken();

        try{
            BatchUpdateMenuResponseDTO batchUpdateMenuResponseDTO = grabMenuSyncCommon.batchUpdateMenuApi(grabFoodClient, token, batchUpdateMenuRequestDTO);
            log.info("Update batch menu merchant {} status is {}", batchUpdateMenuRequestDTO.getMerchantID(), (batchUpdateMenuResponseDTO != null && batchUpdateMenuResponseDTO.getStatus() != null) ? batchUpdateMenuResponseDTO.getStatus() : "");
        }catch (Exception e){
            log.error("Batch update merchant {} menu failed! {}", batchUpdateMenuRequestDTO.getMerchantID(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<Void> batchUpdateMenuReactive(BatchUpdateMenuRequestDTO dto, String token) {
        return grabMenuSyncCommon.batchUpdateMenuReactive(grabFoodClient, dto, token);
    }

    @Override
    public BatchUpdateMenuRequestDTO createBatchUpdate(List<SubmitOrderRequestDTO.Item> items, String merchantId) {
        return grabMenuSyncCommon.createBatchUpdate(items, merchantId, Product.GRAB_FOOD.name());
    }

    @Override
    public BatchUpdateMenuRequestDTO createBatchUpdate(List<SubmitOrderRequestDTO.Item> items, String merchantId, Location location) {
        return grabMenuSyncCommon.createBatchUpdate(items, merchantId, location);
    }
}

package com.oxysystem.general.service.grab.client.grabMart;

import com.oxysystem.general.dto.grab.data.*;
import com.oxysystem.general.dto.grab.view.ListMartCategoryDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.exception.GrabException;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.response.grab.GrabFailedResponse;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.grab.client.common.GrabMenuSyncCommon;
import com.oxysystem.general.service.grab.client.common.interfaces.GrabMenuSyncService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import com.oxysystem.general.service.posmaster.StrukKasirService;
import com.oxysystem.general.service.transaction.GrabMenuSyncStatusService;
import com.oxysystem.general.service.transaction.stock.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.util.*;

@Service
public class GrabMartMenuSyncServiceImpl implements GrabMenuSyncService {
    @Value("${grabmart.endpoint.list-mart-categories}")
    private String endpointListMartCategories;

    private static final Logger LOGGER = LoggerFactory.getLogger(GrabMartMenuSyncServiceImpl.class);

    private final WebClient grabMartClient;
    private final GrabMartOAuthServiceImpl grabMartOAuthService;
    private final StockService stockService;
    private final ItemMasterService itemMasterService;
    private final GrabMenuSyncStatusService grabMenuSyncStatusService;
    private final StrukKasirService strukKasirService;
    private final LocationService locationService;
    private final GrabMenuSyncCommon grabMenuSyncCommon;

    public GrabMartMenuSyncServiceImpl(WebClient grabMartClient, GrabMartOAuthServiceImpl grabMartOAuthService, StockService stockService, ItemMasterService itemMasterService, GrabMenuSyncStatusService grabMenuSyncStatusService, StrukKasirService strukKasirService, LocationService locationService, GrabMenuSyncCommon grabMenuSyncCommon) {
        this.grabMartClient = grabMartClient;
        this.grabMartOAuthService = grabMartOAuthService;
        this.stockService = stockService;
        this.itemMasterService = itemMasterService;
        this.grabMenuSyncStatusService = grabMenuSyncStatusService;
        this.strukKasirService = strukKasirService;
        this.locationService = locationService;
        this.grabMenuSyncCommon = grabMenuSyncCommon;
    }

    @Override
    public ResponseEntity<?> UpdateMenuNotification(String token, UpdateMenuNotificationRequestDTO request) {
        return grabMenuSyncCommon.UpdateMenuNotification(grabMartClient, token, request);
    }

    @Override
    public Mono<Void> updateMenuNotifyReactive(String token, UpdateMenuNotificationRequestDTO request) {
        return grabMenuSyncCommon.updateMenuNotifyReactive(grabMartClient, token, request);
    }

    @Override
    public ResponseEntity<?> listMartCategories(String token, String countryCode) {
        if(countryCode == null || countryCode.isEmpty()) throw new ResourceNotFoundException("country code cannot be empty!");

        ListMartCategoryDTO categories = grabMartClient.get()
                .uri(endpointListMartCategories + "?countryCode=" + countryCode)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse.bodyToMono(GrabFailedResponse.class)
                        .switchIfEmpty(Mono.error(new GrabException("Get list mart category error", "unauthorized")))
                        .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage())))
                )
                .bodyToMono(ListMartCategoryDTO.class)
                .block();

        SuccessResponse<?> response = new SuccessResponse<>("success",categories);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> updateMenuRecord(String token, UpdateMenuRecordRequestDTO updateMenuRecordRequestDTO) {
        return grabMenuSyncCommon.updateMenuRecord(grabMartClient, token, updateMenuRecordRequestDTO);
    }

    @Override
    public ResponseEntity<?> batchUpdateMenu(String token, BatchUpdateMenuRequestDTO batchUpdateMenuRequestDTO) {
         return grabMenuSyncCommon.batchUpdateMenu(grabMartClient, token, batchUpdateMenuRequestDTO);
    }

    @Recover
    public void recover(GrabException ex, String token, UpdateMenuNotificationRequestDTO request) {
        LOGGER.error("Retry exhausted: WebClientRequestException for merchant ID {}: {}", request.getMerchantID(), ex.getMessage(), ex);
    }

    @Recover
    public void recover(WebClientResponseException ex,String token,UpdateMenuNotificationRequestDTO request) {
        LOGGER.error("Retry exhausted: WebClientRequestException for merchant ID {}: {}", request.getMerchantID(), ex.getMessage(), ex);
    }

    @Recover
    public void recover(WebClientRequestException ex,UpdateMenuNotificationRequestDTO request) {
        LOGGER.error("Retry exhausted: WebClientRequestException for merchant ID {}: {}", request.getMerchantID(), ex.getMessage(), ex);
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
        String token = grabMartOAuthService.getGrabToken();

        try{
            BatchUpdateMenuResponseDTO batchUpdateMenuResponseDTO = grabMenuSyncCommon.batchUpdateMenuApi(grabMartClient, token, batchUpdateMenuRequestDTO);
            LOGGER.info("Update batch menu merchant {} status is {}", batchUpdateMenuRequestDTO.getMerchantID(), (batchUpdateMenuResponseDTO != null && batchUpdateMenuResponseDTO.getStatus() != null) ? batchUpdateMenuResponseDTO.getStatus() : "");
        }catch (Exception e){
            LOGGER.error("Batch update merchant {} menu failed! {}", batchUpdateMenuRequestDTO.getMerchantID(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Mono<Void> batchUpdateMenuReactive(BatchUpdateMenuRequestDTO dto, String token) {
        return grabMenuSyncCommon.batchUpdateMenuReactive(grabMartClient, dto, token);
    }

    @Override
    @Transactional
    public BatchUpdateMenuRequestDTO createBatchUpdate(List<SubmitOrderRequestDTO.Item> items, String merchantId){
            return grabMenuSyncCommon.createBatchUpdate(items, merchantId, Product.GRAB_MART.name());
    }

    @Override
    public BatchUpdateMenuRequestDTO createBatchUpdate(List<SubmitOrderRequestDTO.Item> items, String merchantId, Location location) {
        return grabMenuSyncCommon.createBatchUpdate(items, merchantId, location);
    }
}

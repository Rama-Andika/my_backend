package com.oxysystem.general.service.grab.client.common;

import com.oxysystem.general.dto.grab.data.*;
import com.oxysystem.general.dto.transaction.stock.view.StockViewDTO;
import com.oxysystem.general.enums.grab.AvailableStatus;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.exception.GrabException;
import com.oxysystem.general.exception.ResourceUnauthorizedException;
import com.oxysystem.general.model.db1.general.Location;
import com.oxysystem.general.model.db1.posmaster.ItemMaster;
import com.oxysystem.general.model.db1.posmaster.PriceType;
import com.oxysystem.general.model.db1.posmaster.StrukKasir;
import com.oxysystem.general.model.db1.system.SystemMain;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.response.grab.GrabFailedResponse;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import com.oxysystem.general.service.posmaster.StrukKasirService;
import com.oxysystem.general.service.system.SystemMainService;
import com.oxysystem.general.service.transaction.GrabMenuSyncStatusService;
import com.oxysystem.general.service.transaction.stock.StockService;
import com.oxysystem.general.util.PriceTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GrabMenuSyncCommon {
    @Value("${grabmart.endpoint.update-menu-notification}")
    private String endpointUpdateMenuNotification;

    @Value("${grabmart.endpoint.update-menu-record}")
    private String endpointUpdateMenuRecord;

    @Value("${grabmart.endpoint.batch-update-menu-record}")
    private String endpointBatchUpdateMenu;

    private final GrabMenuSyncStatusService grabMenuSyncStatusService;
    private final StrukKasirService strukKasirService;
    private final LocationService locationService;
    private final ItemMasterService itemMasterService;
    private final StockService stockService;
    private final SystemMainService systemMainService;

    public GrabMenuSyncCommon(GrabMenuSyncStatusService grabMenuSyncStatusService, StrukKasirService strukKasirService, LocationService locationService, ItemMasterService itemMasterService, StockService stockService, SystemMainService systemMainService) {
        this.grabMenuSyncStatusService = grabMenuSyncStatusService;
        this.strukKasirService = strukKasirService;
        this.locationService = locationService;
        this.itemMasterService = itemMasterService;
        this.stockService = stockService;
        this.systemMainService = systemMainService;
    }

    public ResponseEntity<?> UpdateMenuNotification(WebClient webClient, String token, UpdateMenuNotificationRequestDTO request) {
        if(token == null || token.isEmpty()) throw new ResourceUnauthorizedException("authorization failed!");

        request.setToken(null);

        Map<String, Object> headers = new HashMap<>();
        webClient.post()
                .uri(endpointUpdateMenuNotification)
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().isError()) {
                        return clientResponse.bodyToMono(GrabFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("Update notification error", "unauthorized")))
                                .flatMap(error -> Mono.error(new GrabException("Update notification error", error.getMessage())));
                    }
                    headers.put("x-job-id", clientResponse.headers().asHttpHeaders().getFirst("x-job-id"));
                    return clientResponse.bodyToMono(String.class);
                })
                .defaultIfEmpty("")
                .block();

        SuccessResponse<?> successResponse = new SuccessResponse<>("success", headers.get("x-job-id").toString());
        return ResponseEntity.ok(successResponse);
    }

    public Mono<Void> updateMenuNotifyReactive(WebClient webClient, String token, UpdateMenuNotificationRequestDTO request) {
        if (token == null || token.isEmpty()) {
            return Mono.error(new ResourceUnauthorizedException("authorization failed!"));
        }

        return webClient.post()
                .uri(endpointUpdateMenuNotification)
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(GrabFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("Update menu notification failed!", "unknown")))
                                .flatMap(errorBody -> {
                                    log.debug("Grab error body: {}", errorBody.getMessage());
                                    return Mono.error(new GrabException(errorBody.getReason(), errorBody.getMessage()));
                                })
                )
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(15))
                .doOnSuccess(v -> log.info("[OK] Updated menu notification for merchantId={}", request.getMerchantID()))
                .doOnError(e -> log.warn("[FAIL] merchantId={} -> {}", request.getMerchantID(), e.getMessage()))
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(1))
                                .maxBackoff(Duration.ofSeconds(10))
                                .jitter(0.5)
                                .filter(throwable -> throwable instanceof WebClientRequestException
                                        || throwable instanceof WebClientResponseException
                                        || throwable instanceof GrabException)
                )
                .then();
    }

    public ResponseEntity<?> updateMenuRecord(WebClient webClient, String token, UpdateMenuRecordRequestDTO updateMenuRecordRequestDTO) {
        webClient.put()
                .uri(endpointUpdateMenuRecord)
                .header("Authorization", "Bearer " + token)
                .bodyValue(updateMenuRecordRequestDTO)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(GrabFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("Update menu failed","unauthorized")))
                                .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage()))))
                .bodyToMono(String.class)
                .switchIfEmpty(Mono.empty())
                .block();

        SuccessResponse<String> successResponse = new SuccessResponse<>("success",null);
        return ResponseEntity.ok(successResponse);
    }

    public BatchUpdateMenuResponseDTO batchUpdateMenuApi(WebClient webClient, String token, BatchUpdateMenuRequestDTO batchUpdateMenuRequestDTO){
        return webClient.put()
                .uri(endpointBatchUpdateMenu)
                .header("Authorization", "Bearer " + token)
                .bodyValue(batchUpdateMenuRequestDTO)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(GrabFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("Update menu failed","unauthorized")))
                                .flatMap(error -> Mono.error(new GrabException(error.getReason(), error.getMessage()))))
                .bodyToMono(BatchUpdateMenuResponseDTO.class)
                .switchIfEmpty(Mono.empty())
                .block();
    }

    public ResponseEntity<?> batchUpdateMenu(WebClient webClient, String token, BatchUpdateMenuRequestDTO batchUpdateMenuRequestDTO) {
        BatchUpdateMenuResponseDTO batchUpdateMenuResponseDTO = batchUpdateMenuApi(webClient, token, batchUpdateMenuRequestDTO);
        SuccessResponse<BatchUpdateMenuResponseDTO> successResponse = new SuccessResponse<>("success",batchUpdateMenuResponseDTO);
        return ResponseEntity.ok(successResponse);
    }

    public Mono<Void> batchUpdateMenuReactive(WebClient webClient, BatchUpdateMenuRequestDTO dto, String token) {
        return webClient.put()
                .uri(endpointBatchUpdateMenu)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(dto)
                .retrieve()
                .onStatus(HttpStatus::isError, clientResponse ->
                        clientResponse.bodyToMono(GrabFailedResponse.class)
                                .switchIfEmpty(Mono.error(new GrabException("Update menu failed","?")))
                                .flatMap(err -> Mono.error(new GrabException(err.getReason(), err.getMessage())))
                )
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(15))
                .doOnSuccess(resp -> log.info("Update batch menu merchant {} success", dto.getMerchantID()))
                .doOnError(e -> log.error("Batch update merchant {} failed", dto.getMerchantID(), e.getMessage()))
                // retry reactive with exponential backoff for transient errors
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .maxBackoff(Duration.ofSeconds(10))
                        .filter(ex -> ex instanceof WebClientRequestException
                                || ex instanceof WebClientResponseException
                                || ex instanceof GrabException)
                        .doBeforeRetry(rs -> log.warn("Retrying update menu for merchant {} attempt {} due to {}",
                                dto.getMerchantID(), rs.totalRetries() + 1, rs.failure().toString()))
                )
                .then();
    }

    public BatchUpdateMenuRequestDTO createBatchUpdate(List<SubmitOrderRequestDTO.Item> items, String merchantId, String product){
        BatchUpdateMenuRequestDTO batchUpdate = new BatchUpdateMenuRequestDTO();
        batchUpdate.setMerchantID(merchantId);
        batchUpdate.setField("ITEM");

        StrukKasir strukKasir = new StrukKasir();
        if(product.equals(Product.GRAB_MART.name())){
            strukKasir = strukKasirService.findByGrabMerchantId(merchantId).orElse(null);
        }else if(product.equals(Product.GRAB_FOOD.name())){
            strukKasir = strukKasirService.findByGrabFoodMerchantId(merchantId).orElse(null);
        }

        if(strukKasir != null){
            Location location = locationService.findById(strukKasir.getLocationId()).orElse(null);
            if(location != null){
                Set<Long> itemMasterIds = items.stream()
                        .map(item -> Long.valueOf(item.getId()))
                        .collect(Collectors.toSet());

                List<ItemMaster> itemMasters = itemMasterService.findItemMasterWithPriceTypeByIds(new ArrayList<>(itemMasterIds));
                Map<Long, ItemMaster> itemMasterMap = itemMasters.stream()
                        .collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity(), (existing, replacement) -> existing));

                List<StockViewDTO> stocks = stockService.getCurrenctStockByItems(new ArrayList<>(itemMasterIds), location.getLocationId());
                Map<Long, Double> stockmap = stocks.stream()
                        .collect(Collectors.toMap(StockViewDTO::getItemMasterId,StockViewDTO::getStock, (existing, replacement) -> existing));

                int multipleBy = (int) Math.pow(10, 2);

                SystemMain systemMainItemIdListDiffZone = systemMainService.findSystemPropertyName("GRABMART_ITEM_ID_LIST_DIFF_ZONE").orElse(null);

                SystemMain systemMainLocationIdListDiffZone = systemMainService.findSystemPropertyName("GRABMART_LOCATION_ID_LIST_DIFF_ZONE").orElse(null);

                String[] itemIdListDiffZone = systemMainItemIdListDiffZone != null ? systemMainItemIdListDiffZone.getValueprop().split(",") : new String[]{""};
                Set<String> itemIdListDiffZoneSet = new HashSet<>(Arrays.asList(itemIdListDiffZone));

                String[] locationIdListDiffZone = systemMainLocationIdListDiffZone != null ? systemMainLocationIdListDiffZone.getValueprop().split(",") : new String[]{""};
                Set<String> locationIdListDiffZoneSet = new HashSet<>(Arrays.asList(locationIdListDiffZone));

                List<BatchUpdateMenuRequestDTO.MenuEntity> menuEntities = items.stream()
                        .map(item -> {
                            ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(item.getId()));
                            if (itemMaster != null){
                                BatchUpdateMenuRequestDTO.MenuEntity menuEntity = new BatchUpdateMenuRequestDTO.MenuEntity();

                                menuEntity.setId(String.valueOf(itemMaster.getItemMasterId()));

                                PriceType priceType = itemMaster.getPriceTypes().stream().min(Comparator.comparing(PriceType::getConvQty, Comparator.nullsLast(Double::compareTo))
                                        .thenComparing(PriceType::getQtyFrom, Comparator.nullsLast(Integer::compareTo))).orElse(null);

                                if(priceType != null){
                                    double price;

                                    if(locationIdListDiffZoneSet.contains(String.valueOf(location.getLocationId())) && itemIdListDiffZoneSet.contains(item.getId())){
                                        price = PriceTypeUtils.getPriceByGol(priceType, "gol_12");
                                    }else {
                                        price = PriceTypeUtils.getPriceByGol(priceType, location.getGolPrice());
                                    }

                                    menuEntity.setPrice((int) (price * multipleBy));
                                }

                                Double maxStock = Optional.of(stockmap.get(Long.valueOf(item.getId()))).orElse(0.0);
                                menuEntity.setAvailableStatus(maxStock <= 0 ? AvailableStatus.UNAVAILABLE : AvailableStatus.AVAILABLE);
                                menuEntity.setMaxStock(maxStock.intValue());
                                return menuEntity;
                            }
                            return null;
                        }).collect(Collectors.toList());
                if(!menuEntities.isEmpty()) batchUpdate.setMenuEntities(menuEntities);
            }

        }

        return batchUpdate;
    }

    public BatchUpdateMenuRequestDTO createBatchUpdate(List<SubmitOrderRequestDTO.Item> items, String merchantId, Location location) {
        BatchUpdateMenuRequestDTO batchUpdate = new BatchUpdateMenuRequestDTO();
        batchUpdate.setMerchantID(merchantId);
        batchUpdate.setField("ITEM");

        Set<Long> itemMasterIds = items.stream()
                .map(item -> Long.valueOf(item.getId()))
                .collect(Collectors.toSet());

        List<ItemMaster> itemMasters = itemMasterService.findItemMasterWithPriceTypeByIds(new ArrayList<>(itemMasterIds));
        Map<Long, ItemMaster> itemMasterMap = itemMasters.stream()
                .collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity(), (existing, replacement) -> existing));

        List<StockViewDTO> stocks = stockService.getCurrenctStockByItems(new ArrayList<>(itemMasterIds), location.getLocationId());
        Map<Long, Double> stockmap = stocks.stream()
                .collect(Collectors.toMap(StockViewDTO::getItemMasterId,StockViewDTO::getStock, (existing, replacement) -> existing));

        int multipleBy = (int) Math.pow(10, 2);

        SystemMain systemMainItemIdListDiffZone = systemMainService.findSystemPropertyName("GRABMART_ITEM_ID_LIST_DIFF_ZONE").orElse(null);

        SystemMain systemMainLocationIdListDiffZone = systemMainService.findSystemPropertyName("GRABMART_LOCATION_ID_LIST_DIFF_ZONE").orElse(null);

        String[] itemIdListDiffZone = systemMainItemIdListDiffZone != null ? systemMainItemIdListDiffZone.getValueprop().split(",") : new String[]{""};
        Set<String> itemIdListDiffZoneSet = new HashSet<>(Arrays.asList(itemIdListDiffZone));

        String[] locationIdListDiffZone = systemMainLocationIdListDiffZone != null ? systemMainLocationIdListDiffZone.getValueprop().split(",") : new String[]{""};
        Set<String> locationIdListDiffZoneSet = new HashSet<>(Arrays.asList(locationIdListDiffZone));

        List<BatchUpdateMenuRequestDTO.MenuEntity> menuEntities = items.stream()
                .map(item -> {
                    ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(item.getId()));
                    if (itemMaster != null){
                        BatchUpdateMenuRequestDTO.MenuEntity menuEntity = new BatchUpdateMenuRequestDTO.MenuEntity();

                        menuEntity.setId(String.valueOf(itemMaster.getItemMasterId()));

                        PriceType priceType = itemMaster.getPriceTypes().stream().min(Comparator.comparing(PriceType::getConvQty, Comparator.nullsLast(Double::compareTo))
                                .thenComparing(PriceType::getQtyFrom, Comparator.nullsLast(Integer::compareTo))).orElse(null);

                        if(priceType != null){
                            double price;

                            if(locationIdListDiffZoneSet.contains(String.valueOf(location.getLocationId())) && itemIdListDiffZoneSet.contains(item.getId())){
                                price = PriceTypeUtils.getPriceByGol(priceType, "gol_12");
                            }else {
                                price = PriceTypeUtils.getPriceByGol(priceType, location.getGolPrice());
                            }

                            menuEntity.setPrice((int) (price * multipleBy));
                        }

                        Double maxStock = Optional.of(stockmap.get(Long.valueOf(item.getId()))).orElse(0.0);
                        menuEntity.setAvailableStatus(maxStock <= 0 ? AvailableStatus.UNAVAILABLE : AvailableStatus.AVAILABLE);
                        menuEntity.setMaxStock(maxStock.intValue());
                        return menuEntity;
                    }
                    return null;
                }).collect(Collectors.toList());
        if(!menuEntities.isEmpty()) batchUpdate.setMenuEntities(menuEntities);

        return batchUpdate;
    }
}

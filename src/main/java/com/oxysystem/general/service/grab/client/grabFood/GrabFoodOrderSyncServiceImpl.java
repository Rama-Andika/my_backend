package com.oxysystem.general.service.grab.client.grabFood;

import com.oxysystem.general.dto.grab.data.ListOrderResponseDTO;
import com.oxysystem.general.dto.grab.view.OrderDTO;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.enums.grab.StateStatus;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.model.tenant.posmaster.StrukKasir;
import com.oxysystem.general.model.tenant.transaction.sales.Sales;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.grab.client.common.GrabOrderSyncCommon;
import com.oxysystem.general.service.grab.client.common.interfaces.GrabOrderSyncService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import com.oxysystem.general.service.posmaster.StrukKasirService;
import com.oxysystem.general.service.transaction.sales.SalesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GrabFoodOrderSyncServiceImpl implements GrabOrderSyncService {
    private final WebClient grabFoodClient;
    private final LocationService locationService;
    private final GrabFoodOAuthServiceImpl grabFoodOAuthService;
    private final StrukKasirService strukKasirService;
    private final ItemMasterService itemMasterService;
    private final SalesService salesService;
    private final GrabOrderSyncCommon grabOrderSyncCommon;

    public GrabFoodOrderSyncServiceImpl(WebClient grabFoodClient, LocationService locationService, GrabFoodOAuthServiceImpl grabFoodOAuthService, StrukKasirService strukKasirService, ItemMasterService itemMasterService, SalesService salesService, GrabOrderSyncCommon grabOrderSyncCommon) {
        this.grabFoodClient = grabFoodClient;
        this.locationService = locationService;
        this.grabFoodOAuthService = grabFoodOAuthService;
        this.strukKasirService = strukKasirService;
        this.itemMasterService = itemMasterService;
        this.salesService = salesService;
        this.grabOrderSyncCommon = grabOrderSyncCommon;
    }

    @Override
    public ResponseEntity<?> listOrder(String token, String merchantID, String date, Integer page, List<String> orderIds) {
        return grabOrderSyncCommon.listOrder(grabFoodClient, token, merchantID, date, page, orderIds);
    }

    @Override
    public ListOrderResponseDTO getListOrder(String token, String merchantID, String date, Integer page, List<String> orderIDs) {
        return grabOrderSyncCommon.getListOrder(grabFoodClient, token, merchantID, date, page, orderIDs);
    }

    @Override
    public ResponseEntity<?> listOrderMappingToListOrderDTO(Long locationId, String date, Integer page, List<String> orderIDs) {
        String token = grabFoodOAuthService.getGrabToken();
        if(locationId == null) throw new ResourceNotFoundException("location cannot be empty!");

        Location location = locationService.findById(locationId).orElseThrow(() -> new ResourceNotFoundException("location not found!"));
        StrukKasir strukKasir = strukKasirService.findStrukKasirByLocationId(locationId).orElseThrow(() -> new ResourceNotFoundException("grab merchant has not been setup yet!"));

        if(strukKasir.getGrabFoodMerchantId() == null || strukKasir.getGrabFoodMerchantId().isEmpty()) throw new ResourceNotFoundException("merchant ID cannot be empty!");

        String merchantID = strukKasir.getGrabFoodMerchantId();

        ListOrderResponseDTO listOrderResponseDTO = grabOrderSyncCommon.getListOrder(grabFoodClient, token, merchantID, date, page, orderIDs);

        OrderDTO orderDTO = new OrderDTO();
        if(listOrderResponseDTO != null){
            orderDTO.setMore(listOrderResponseDTO.getMore());

            Set<Long> itemIDs = listOrderResponseDTO.getOrders().stream()
                    .flatMap(order -> order.getItems().stream())
                    .map(item -> Long.valueOf(item.getId()))
                    .collect(Collectors.toSet());

            Set<String> numbers = listOrderResponseDTO.getOrders().stream()
                    .map(ListOrderResponseDTO.Order::getOrderID)
                    .collect(Collectors.toSet());

            List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemIDs));
            List<Sales> sales = salesService.findAllSalesByNumber(new ArrayList<>(numbers));

            Map<Long, ItemMaster> itemMasterMap = itemMasters.stream()
                    .collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity(),(existing, replacement) -> existing));

            Map<String, Sales> salesMap = sales.stream()
                    .collect(Collectors.toMap(Sales::getNumber, Function.identity()));

            orderDTO.setOrders(
                    listOrderResponseDTO.getOrders().stream()
                            .map(o -> {
                                LocalDateTime orderDate = LocalDateTime.ofInstant(o.getOrderTime(), ZoneOffset.ofHours(8));

                                int divisionBy = (o.getCurrency() != null && o.getCurrency().getExponent() != null) ? (int) Math.pow(10, o.getCurrency().getExponent()) : 10;
                                Integer amount = o.getPrice().getTotal() / divisionBy;
                                Integer discountAmount = o.getPrice().getMerchantFundPromo() / divisionBy;

                                List<OrderDTO.DetailOrder.Item> items = o.getItems().stream()
                                        .map(i -> {
                                            ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(i.getId()));

                                            OrderDTO.DetailOrder.Item item = new OrderDTO.DetailOrder.Item();
                                            if(itemMaster != null){
                                                item.setId(i.getId());
                                                item.setName(itemMaster.getName());
                                                item.setCode(itemMaster.getCode());
                                                item.setBarcode(itemMaster.getBarcode());
                                                item.setQty(i.getQuantity());
                                                item.setPrice(i.getPrice());
                                            }

                                            return item;
                                        }).collect(Collectors.toList());

                                OrderDTO.DetailOrder order = new OrderDTO.DetailOrder();
                                order.setOrderDate(orderDate);
                                order.setOrderNumber(o.getOrderID());
                                order.setStore(location.getName());
                                order.setStoreId(String.valueOf(location.getLocationId()));
                                order.setAmount(amount);
                                order.setDiscountAmount(discountAmount);
                                order.setGrabStatus(o.getOrderState());

                                Sales s = salesMap.get(o.getOrderID());
                                if(s != null){
                                    order.setStatus(DocumentStatus.UPLOADED.name());
                                }else {
                                    if(order.getGrabStatus().equals(StateStatus.CANCELLED.name()) || order.getGrabStatus().equals(StateStatus.FAILED.name())){
                                        order.setStatus(DocumentStatus.CANCELED.name());
                                    }else{
                                        order.setStatus(DocumentStatus.PENDING.name());
                                    }
                                }
                                order.setItems(items);
                                return order;
                            }).collect(Collectors.toList())
            );
        }

        SuccessResponse<OrderDTO> response = new SuccessResponse<>("success",orderDTO);
        return ResponseEntity.ok(response);
    }

    @Override
    public Mono<ListOrderResponseDTO> getListOrderReactive(String token, String merchantID, String date, Integer page, List<String> orderIDs) {
        return grabOrderSyncCommon.getListOrderReactive(grabFoodClient, token, merchantID, date, page, orderIDs);
    }
}

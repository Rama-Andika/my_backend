package com.oxysystem.general.service.transaction.sales.impl;

import com.oxysystem.general.dto.general.dashboard.view.DashboardSummaryGrabDTO;
import com.oxysystem.general.dto.general.dashboard.view.RecentSalesGrabDTO;
import com.oxysystem.general.dto.grab.data.CampaignMapDTO;
import com.oxysystem.general.dto.grab.data.ListOrderResponseDTO;
import com.oxysystem.general.dto.grab.data.SubmitOrderRequestDTO;
import com.oxysystem.general.dto.transaction.sales.view.SalesViewDTO;
import com.oxysystem.general.enums.grab.CampaignLevel;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.model.tenant.posmaster.Payment;
import com.oxysystem.general.model.tenant.system.SystemMain;
import com.oxysystem.general.model.tenant.transaction.sales.Sales;
import com.oxysystem.general.model.tenant.transaction.sales.SalesDetail;
import com.oxysystem.general.repository.tenant.transaction.sales.SalesRepository;
import com.oxysystem.general.service.posmaster.*;
import com.oxysystem.general.service.system.SystemMainService;
import com.oxysystem.general.service.transaction.sales.SalesService;
import com.oxysystem.general.util.NumberUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalesServiceImpl implements SalesService {
    private final SalesRepository salesRepository;
    private final PaymentService paymentService;
    private final SystemMainService systemMainService;

    public SalesServiceImpl(SalesRepository salesRepository, PaymentService paymentService, SystemMainService systemMainService) {
        this.salesRepository = salesRepository;
        this.paymentService = paymentService;
        this.systemMainService = systemMainService;
    }

    @Override
    public Sales save(Sales sales) {
        return salesRepository.save(sales);
    }

    @Override
    public Optional<Integer> countSalesByCashCashierId(Long id) {
        return salesRepository.countSalesByCashCashierId(id);
    }

    @Override
    public  Optional<Integer> countSalesDetailByCashCashierId(Long id) {
        return salesRepository.countSalesDetailByCashCashierId(id);
    }

    @Override
    public DashboardSummaryGrabDTO mappingToDashboardSummaryByCashMasterType(int type) {
        return salesRepository.mappingToDashboardSummaryByCashMasterType(type);
    }

    @Override
    public List<RecentSalesGrabDTO> recentSalesByCashMasterType(int type) {
        return salesRepository.recentSalesByCashMasterType(type);
    }

    @Override
    public Optional<Sales> findSalesByNumber(String number) {
        return salesRepository.findSalesByNumber(number);
    }

    @Override
    public List<Sales> findAllSalesByNumber(List<String> numbers) {
        return salesRepository.findAllSalesByNumber(numbers);
    }

    @Override
    public Flux<SalesViewDTO> getSalesGrabSplitOrderReactive(String date, String userId) {
        return Flux.defer(() -> Flux.fromIterable(salesRepository.getSalesGrabSplitOrder(date, userId)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<Void> grabSplitOrderFixDiscountReactive(List<ListOrderResponseDTO.Order> orders, String product) {
        return Mono.fromCallable(() -> {
            Map<String, ListOrderResponseDTO.Order> orderMap = orders.stream()
                    .collect(Collectors.toMap(ListOrderResponseDTO.Order::getOrderID, Function.identity()));

            Set<String> orderIds = orders.stream()
                    .map(ListOrderResponseDTO.Order::getOrderID)
                    .collect(Collectors.toSet());

            List<Sales> listSales = findAllSalesByNumber(new ArrayList<>(orderIds));

            Set<Long> salesIds = listSales.stream().map(Sales::getSalesId).collect(Collectors.toSet());

            List<Payment> payments = paymentService.getPaymentsBySalesIds(new ArrayList<>(salesIds));

            Map<Long, List<Payment>> mapPayments = payments.stream().collect(Collectors.groupingBy(r -> r.getSales().getSalesId()));

            List<Sales> listSalesUpdate = new ArrayList<>();
            List<Payment>  listPaymentUpdate = new ArrayList<>();

            // ====== SYSTEM MAIN =====
            SystemMain systemMainGrabMerchantName = new SystemMain();
            if (product.equals(Product.GRAB_MART.name())){
                systemMainGrabMerchantName = systemMainService.findSystemPropertyName("GRABMART_MERCHANT_NAME").orElse(null);
            }else if (product.equals(Product.GRAB_FOOD.name())){
                systemMainGrabMerchantName = systemMainService.findSystemPropertyName("GRABFOOD_MERCHANT_NAME").orElse(null);
            }

            // ====== END SYSTEM MAIN ======

            // ====== CAMPAIGN =====
            Map<String, CampaignMapDTO> campaignWithItemMap = new HashMap<>();
            Map<String, CampaignMapDTO> campaignWithoutItemMap = new HashMap<>();
            Map<String, BigDecimal> totalSalesPerCampaignMap = new HashMap<>();

            for(ListOrderResponseDTO.Order order : orders){
                int divisionBy = order.getCurrency().getExponent() != null ? (int) Math.pow(10, order.getCurrency().getExponent()) : 10;

                if(order.getCampaigns() != null && !order.getCampaigns().isEmpty()){
                    for(SubmitOrderRequestDTO.Campaign campaign: order.getCampaigns()){
                        CampaignMapDTO campaignMapDTO = new CampaignMapDTO(campaign.getId(), campaign.getLevel(), campaign.getType(),campaign.getDeductedAmount() / divisionBy, campaign.getMexFundedRatio());

                        BigDecimal totalSalesPerCampaign = BigDecimal.ZERO;

                        if(campaign.getAppliedItemIDs() != null && !campaign.getAppliedItemIDs().isEmpty()){
                            for(String itemId: campaign.getAppliedItemIDs()){
                                campaignWithItemMap.put(itemId, campaignMapDTO);

                                SubmitOrderRequestDTO.Item item = order.getItems().stream().filter(i -> i.getId().equals(itemId)).findFirst().orElse(null);
                                if(item != null){
                                    int price = item.getPrice() / divisionBy;
                                    BigDecimal total = NumberUtils.numberScale(BigDecimal.valueOf(Optional.of(price * item.getQuantity()).orElse(0)));

                                    totalSalesPerCampaign = totalSalesPerCampaign.add(total);
                                }
                            }
                        }

                        BigDecimal totalSalesCampaignPerMapPrev = totalSalesPerCampaignMap.get(campaign.getId());
                        if(totalSalesCampaignPerMapPrev != null){
                            totalSalesPerCampaignMap.put(campaign.getId(), totalSalesPerCampaign.add(totalSalesCampaignPerMapPrev));
                        }else{
                            totalSalesPerCampaignMap.put(campaign.getId(), totalSalesPerCampaign);
                        }

                        campaignWithoutItemMap.put(campaign.getLevel(), campaignMapDTO);

                    }
                }

            }
            // ===== END CAMPAIGN =====

            // looping sales
            for(Sales sales: listSales){
                ListOrderResponseDTO.Order order = orderMap.get(sales.getNumber());

                if(order == null) continue;

                int divisionBy = order.getCurrency().getExponent() != null ? (int) Math.pow(10, order.getCurrency().getExponent()) : 10;
                BigDecimal merchantFundPromo = NumberUtils.numberScale(BigDecimal.valueOf(order.getPrice().getMerchantFundPromo() / divisionBy));
                int subTotalTransaction = Optional.of(order.getPrice().getSubtotal()).orElse(0) / divisionBy;


                BigDecimal remainingMerchantFundPromo = merchantFundPromo;

                // looping sales detail
                for(SalesDetail salesDetail: sales.getSalesDetails()){
                    ItemMaster itemMaster = salesDetail.getItemMaster();

                    salesDetail.setTotal(NumberUtils.numberScale(BigDecimal.valueOf(Optional.of(salesDetail.getSellingPrice() * salesDetail.getQty()).orElse((double) 0))));

                    BigDecimal ratio;
                    BigDecimal discountGlobal;
                    BigDecimal discountItem = BigDecimal.ZERO;
                    // Get campaign by item id
                    CampaignMapDTO campaignByItem = campaignWithItemMap.get(String.valueOf(itemMaster.getItemMasterId()));

                    if(campaignByItem != null){
                        BigDecimal totalSalesPerCampaign = totalSalesPerCampaignMap.get(campaignByItem.getId());

                        if(totalSalesPerCampaign != null){
                            ratio = salesDetail.getTotal().divide(totalSalesPerCampaign,14, RoundingMode.HALF_UP);

                            BigDecimal deductedAmount = BigDecimal.valueOf(campaignByItem.getDeductedAmount());
                            BigDecimal ratioDeductedAmount = BigDecimal.valueOf(campaignByItem.getMexFundedRatio())
                                    .divide(BigDecimal.valueOf(100), 14, RoundingMode.HALF_UP);

                            BigDecimal totalDeductedAmount = deductedAmount.multiply(ratioDeductedAmount);

                            discountItem = NumberUtils.numberScale(totalDeductedAmount.multiply(ratio));
                        }

                        salesDetail.setDiscountItem(discountItem);
                    }else{
                        // Get mapping campaign type order
                        CampaignMapDTO campaignMapLevelOrder = campaignWithoutItemMap.get(CampaignLevel.order.name());
                        if(campaignMapLevelOrder != null){
                            ratio = salesDetail.getTotal().divide(BigDecimal.valueOf(subTotalTransaction),14, RoundingMode.HALF_UP);
                            discountGlobal = NumberUtils.numberScale(merchantFundPromo.multiply(ratio));
                            remainingMerchantFundPromo = remainingMerchantFundPromo.subtract(discountGlobal);

                            salesDetail.setDiscountGlobal(discountGlobal);
                        }


                        BigDecimal discountAmount = NumberUtils.numberScale(Optional.of(salesDetail.getDiscountItem().add(salesDetail.getDiscountGlobal())).orElse(BigDecimal.valueOf(0)));
                        salesDetail.setDiscountAmount(discountAmount);
                        salesDetail.setTotal(salesDetail.getTotal().subtract(salesDetail.getDiscountAmount()));
                    }
                }

                BigDecimal grandTotal = sales.getSalesDetails().stream().map(SalesDetail::getTotal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

                // payment
                String merchantName = systemMainGrabMerchantName != null ? systemMainGrabMerchantName.getValueprop() : "";

                Payment payment = mapPayments.get(sales.getSalesId()).stream()
                        .filter(p -> p.getMerchant().getDescription().equalsIgnoreCase(merchantName))
                        .findFirst().orElse(null);

                if(payment != null){
                    payment.setAmount(grandTotal.doubleValue());
                    listPaymentUpdate.add(payment);
                }
                // end payment

                listSalesUpdate.add(sales);
            }

            if(!listSalesUpdate.isEmpty()) salesRepository.saveAll(listSalesUpdate);
            if(!listPaymentUpdate.isEmpty()) paymentService.saveAll(listPaymentUpdate);

            return null;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }


}

package com.oxysystem.general.service.grab.partner.common;

import com.oxysystem.general.dto.general.company.view.CompanyViewDTO;
import com.oxysystem.general.dto.grab.data.*;
import com.oxysystem.general.dto.grab.view.CurrencyDTO;
import com.oxysystem.general.dto.posmaster.cogs.view.CogsViewDTO;
import com.oxysystem.general.enums.CashMasterType;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.enums.SalesType;
import com.oxysystem.general.enums.TypeItem;
import com.oxysystem.general.enums.grab.CampaignLevel;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.enums.grab.StateStatus;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.model.tenant.general.*;
import com.oxysystem.general.model.tenant.general.Currency;
import com.oxysystem.general.model.tenant.posmaster.*;
import com.oxysystem.general.model.tenant.system.SystemMain;
import com.oxysystem.general.model.tenant.transaction.sales.Sales;
import com.oxysystem.general.model.tenant.transaction.sales.SalesDetail;
import com.oxysystem.general.model.tenant.transaction.sales.SalesGrab;
import com.oxysystem.general.model.tenant.transaction.sales.SalesGrabDetail;
import com.oxysystem.general.model.tenant.transaction.stock.Stock;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.admin.UserService;
import com.oxysystem.general.service.general.*;
import com.oxysystem.general.service.grab.client.common.interfaces.GrabMenuSyncService;
import com.oxysystem.general.service.grab.client.common.interfaces.GrabOAuthService;
import com.oxysystem.general.service.grab.client.common.interfaces.GrabOrderSyncService;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOAuthServiceImpl;
import com.oxysystem.general.service.grab.client.grabFood.GrabFoodOrderSyncServiceImpl;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartOAuthServiceImpl;
import com.oxysystem.general.service.grab.client.grabMart.GrabMartOrderSyncServiceImpl;
import com.oxysystem.general.service.posmaster.*;
import com.oxysystem.general.service.system.SystemMainService;
import com.oxysystem.general.service.transaction.sales.SalesGrabDetailService;
import com.oxysystem.general.service.transaction.sales.SalesGrabService;
import com.oxysystem.general.service.transaction.sales.SalesService;
import com.oxysystem.general.service.transaction.stock.StockService;
import com.oxysystem.general.util.GrabNumberChecker;
import com.oxysystem.general.util.NumberUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GrabOrderSyncOrderPartnerCommon {
    private final GrabMartOAuthServiceImpl grabMartOAuthService;
    private final GrabFoodOAuthServiceImpl grabFoodOAuthService;
    private final GrabMartOrderSyncServiceImpl grabMartOrderSyncService;
    private final GrabFoodOrderSyncServiceImpl grabFoodOrderSyncService;
    private final StrukKasirService strukKasirService;
    private final LocationService locationService;
    private final SalesGrabService salesGrabService;
    private final SalesGrabDetailService salesGrabDetailService;
    private final StockService stockService;
    private final SystemMainService systemMainService;
    private final UserService userService;
    private final ItemMasterService itemMasterService;
    private final CustomerService customerService;
    private final CurrencyService currencyService;
    private final CashMasterService cashMasterService;
    private final CashCashierService cashCashierService;
    private final CompanyService companyService;
    private final CogsService cogsService;
    private final SalesService salesService;
    private final MerchantService merchantService;
    private final PaymentService paymentService;
    private final ReturnPaymentService returnPaymentService;
    private final ShiftService shiftService;
    private final GrabPackagingItemService grabPackagingItemService;
    private final RecipeService recipeService;

    public <T extends GrabOAuthService> String getGrabToken(T grabOAuthService){
        return grabOAuthService.getGrabToken();
    }

    public ListOrderResponseDTO.Order getGrabOrder(PushOrderStateRequestDTO pushOrderStateRequestDTO, String product){
        String token = "";

        if(product.equals(Product.GRAB_MART.name())){
            token = getGrabToken(grabMartOAuthService);
        }else if(product.equals(Product.GRAB_FOOD.name())){
            token = getGrabToken(grabFoodOAuthService);
        }

        List<String> orderIDs = new ArrayList<>();
        orderIDs.add(pushOrderStateRequestDTO.getOrderID());

        ListOrderResponseDTO listOrderResponseDTO = new ListOrderResponseDTO();

        if(product.equals(Product.GRAB_MART.name())){
            listOrderResponseDTO = grabMartOrderSyncService.getListOrder(token, pushOrderStateRequestDTO.getMerchantID(),null,null,orderIDs);
        }else if(product.equals(Product.GRAB_FOOD.name())){
            listOrderResponseDTO = grabFoodOrderSyncService.getListOrder(token, pushOrderStateRequestDTO.getMerchantID(),null,null,orderIDs);
        }

        Optional<ListOrderResponseDTO.Order> optOrder = listOrderResponseDTO.getOrders().stream().findFirst();
        return optOrder.orElse(null);

    }

    public <T extends GrabMenuSyncService> void batchUpdateMenu(T grabMenuSyncService, List<SubmitOrderRequestDTO.Item> items, String merchantID){
        // ======== Update menu jika ada perubahan ========
        BatchUpdateMenuRequestDTO batchUpdate = grabMenuSyncService.createBatchUpdate(items, merchantID);
        if (!batchUpdate.getMenuEntities().isEmpty()) {
            try {
                grabMenuSyncService.batchUpdateMenu(batchUpdate);
            } catch (Exception ignored) {
            }
        }
    }

    private LocalDateTime getLocalDateTime(Instant completeTime, Instant submitTime, Instant orderTime) {
        LocalDateTime date;
        if(completeTime != null){
            date = LocalDateTime.ofInstant(completeTime, ZoneOffset.ofHours(8));
        }else if (submitTime != null){
            date = LocalDateTime.ofInstant(submitTime,ZoneOffset.ofHours(8));
        }else{
            date = LocalDateTime.ofInstant(orderTime, ZoneOffset.ofHours(8));
        }
        return date;
    }

    private StrukKasir getStrukKasir(String product, String merchantID){
        StrukKasir strukKasir = new StrukKasir();

        if(product.equals(Product.GRAB_MART.name())){
            strukKasir = strukKasirService.findByGrabMerchantId(merchantID)
                    .orElseThrow(() -> new ResourceNotFoundException("merchant ID has not been setup yet!"));
        }else if(product.equals(Product.GRAB_FOOD.name())){
            strukKasir = strukKasirService.findByGrabFoodMerchantId(merchantID)
                    .orElseThrow(() -> new ResourceNotFoundException("merchant ID has not been setup yet!"));
        }

        return strukKasir;
    }

    private User getUser(String product){
        String userId;
        Optional<SystemMain> optSystemMain = Optional.empty();
        if(product.equals(Product.GRAB_MART.name())){
            optSystemMain = systemMainService.findSystemPropertyName("GRABMART_SALES_USER_ID");
        }else if(product.equals(Product.GRAB_FOOD.name())){
            optSystemMain = systemMainService.findSystemPropertyName("GRABFOOD_SALES_USER_ID");
        }

        if (optSystemMain.isPresent()) {
            userId = optSystemMain.get().getValueprop();
        } else {
            return userService.findUserAdmin()
                    .orElseThrow(() -> new ResourceNotFoundException("grab sales user not found!"));
        }

        return userService.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ResourceNotFoundException("grab user sales has not been setup yet!"));
    }

    private SubmitOrderRequestDTO getSubmitOrderRequestDTO(ListOrderResponseDTO.Order order) {
        SubmitOrderRequestDTO submitOrderRequestDTO = new SubmitOrderRequestDTO();
        submitOrderRequestDTO.setOrderID(order.getOrderID());
        submitOrderRequestDTO.setShortOrderNumber(order.getShortOrderNumber());
        submitOrderRequestDTO.setMerchantID(order.getMerchantID());
        submitOrderRequestDTO.setPartnerMerchantID(order.getPartnerMerchantID());
        submitOrderRequestDTO.setPaymentType(order.getPaymentType());
        submitOrderRequestDTO.setOrderTime(order.getOrderTime());
        submitOrderRequestDTO.setCompleteTime(order.getCompleteTime());
        submitOrderRequestDTO.setScheduledTime(order.getScheduledTime());
        submitOrderRequestDTO.setOrderState(order.getOrderState());
        submitOrderRequestDTO.setCurrency(order.getCurrency());
        submitOrderRequestDTO.setFeatureFlags(order.getFeatureFlags());
        submitOrderRequestDTO.setItems(order.getItems());
        submitOrderRequestDTO.setCampaigns(order.getCampaigns());
        submitOrderRequestDTO.setPromos(order.getPromos());
        submitOrderRequestDTO.setPrice(order.getPrice());
        submitOrderRequestDTO.setReceiver(order.getReceiver());
        submitOrderRequestDTO.setOrderReadyEstimation(order.getOrderReadyEstimation());
        submitOrderRequestDTO.setMembershipID(order.getMembershipID());
        return submitOrderRequestDTO;
    }

    private Optional<CashMaster> getCashMaster(String product, Long locationID){
        Optional<CashMaster> optCashMaster = Optional.empty();

        if(product.equals(Product.GRAB_MART.name())){
            optCashMaster = cashMasterService.findCashMasterByType(locationID, CashMasterType.GRAB_POS.ordinal());
        } else if (product.equals(Product.GRAB_FOOD.name())) {
            optCashMaster = cashMasterService.findCashMasterByType(locationID, CashMasterType.GRAB_FOOD_POS.ordinal());
        }

        return optCashMaster;
    }

    public List<Stock> submitOrder(SubmitOrderRequestDTO submitOrderRequestDTO, String product) {
        List<SubmitOrderRequestDTO.Item> items = submitOrderRequestDTO.getItems();

        String orderID = submitOrderRequestDTO.getOrderID();

        List<Stock> stocks = new ArrayList<>();

        if (items.isEmpty()) {
            log.warn("Order {} received with empty items, skipping", orderID);
            return stocks;
        }

        String merchantID = submitOrderRequestDTO.getMerchantID();

        StrukKasir strukKasir = getStrukKasir(product, merchantID);

        Location location = locationService.findById(strukKasir.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("location not found!"));

        CurrencyDTO currency = submitOrderRequestDTO.getCurrency();

        List<SalesGrabDetail> salesGrabDetails = new ArrayList<>();

        // tentukan tanggal order
        LocalDateTime date = getLocalDateTime(submitOrderRequestDTO.getCompleteTime(), submitOrderRequestDTO.getSubmitTime(), submitOrderRequestDTO.getOrderTime());

        log.info("Start submit order {}", orderID);

        // ======== CEK EXISTING ORDER UNTUK IDEMPOTENCY ========
        Optional<SalesGrab> existingOrder = salesGrabService.findSalesGrabByNumber(orderID);
        boolean isEdit = Boolean.TRUE.equals(submitOrderRequestDTO.getFeatureFlags().getMexEditOrder());

        if (existingOrder.isPresent() && !isEdit) {
            log.info("Duplicate order {} detected with no edit flag, skipping insert.", orderID);
            return stocks;
        }

        // Jika edit order → hapus dulu data lama
        if (isEdit) {
            log.info("Edit order {} detected, cleaning up old data", orderID);
            salesGrabDetailService.deleteSalesGrabDetailBySalesGrabNumber(orderID);
            salesGrabService.deleteSalesGrabByNumber(orderID);
            stockService.deleteStockByNoFaktur(orderID);
        }

        // ======== Grab User (sales representative) ========
        User user = getUser(product);

        // ======== Item Master Map ========
        Set<Long> itemIds = items.stream()
                .map(item -> Long.valueOf(item.getId()))
                .collect(Collectors.toSet());

        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemIds));

        Map<Long, ItemMaster> itemMasterMap = itemMasters.stream()
                .collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity(), (a, b) -> a));

        // ======== Sales Grab ========
        SalesGrab salesGrab = new SalesGrab();
        salesGrab.setNumber(orderID);
        salesGrab.setDate(date);
        salesGrab.setStatus(DocumentStatus.DRAFT);

        // Customer setup
        Customer customer;

        String phones = submitOrderRequestDTO.getReceiver() != null && submitOrderRequestDTO.getReceiver().getPhones() != null ? submitOrderRequestDTO.getReceiver().getPhones() : null;

        if (phones != null && !phones.isEmpty()) {
            customer = customerService.findCustomerByCode(phones)
                    .orElseGet(() -> customerService.findCustomerPublic()
                            .orElseThrow(() -> new ResourceNotFoundException("public customer has not been setup yet!")));
        } else {
            customer = customerService.findCustomerPublic()
                    .orElseThrow(() -> new ResourceNotFoundException("customer public has been not setup yet"));
        }
        salesGrab.setCustomer(customer);
        salesGrab.setLocation(location);

        int divisionBy = (currency != null && currency.getExponent() != null)
                ? (int) Math.pow(10, currency.getExponent())
                : 10;

        int merchantChargeFee = Optional.ofNullable(submitOrderRequestDTO.getPrice().getMerchantChargeFee()).orElse(0) / divisionBy;

        salesGrab.setAmount((double) submitOrderRequestDTO.getPrice().getSubtotal() / divisionBy);

        String orderState = submitOrderRequestDTO.getOrderState();
        DocumentStatus status = orderState.equalsIgnoreCase(StateStatus.DELIVERED.name()) ? DocumentStatus.APPROVED : DocumentStatus.RESERVED;

        //This bunch of code is for searching item recipe
        List<Recipe> recipes = recipeService.findByItemMasterIds(new  ArrayList<>(itemIds));
        Map<Long, List<Recipe>> recipeMap = recipes.stream().collect(Collectors.groupingBy(r -> r.getItemMaster().getItemMasterId()));

        // isi detail & stock
        for (SubmitOrderRequestDTO.Item item : items) {
            ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(item.getId()));
            if (itemMaster != null) {
                double price = (double) item.getPrice() / divisionBy;

                // Sales Grab Detail
                SalesGrabDetail salesGrabDetail = new SalesGrabDetail();
                salesGrabDetail.setSalesGrab(salesGrab);
                salesGrabDetail.setItemMaster(itemMaster);
                salesGrabDetail.setSellingPrice(price);
                salesGrabDetail.setQty(item.getQuantity().doubleValue());
                BigDecimal total = NumberUtils.numberScale(BigDecimal
                        .valueOf(salesGrabDetail.getSellingPrice())
                        .multiply(BigDecimal.valueOf(salesGrabDetail.getQty())));
                salesGrabDetail.setTotal(total);
                salesGrabDetails.add(salesGrabDetail);

                // Stock
                if(itemMaster.getNeedRecipe() == 0){
                    Stock stock = new Stock();
                    stock.setLocation(location);
                    stock.setType(7);
                    stock.setQty(Double.valueOf(item.getQuantity()));
                    stock.setPrice(price);
                    stock.setTotal(item.getQuantity() * price);
                    stock.setItemMaster(itemMaster);
                    stock.setItemCode(itemMaster.getCode());
                    stock.setItemBarcode(itemMaster.getBarcode());
                    stock.setItemName(itemMaster.getName());
                    stock.setUom(itemMaster.getUomStock());
                    stock.setUnit(itemMaster.getUomStock().getUnit());
                    stock.setInOut(-1);
                    stock.setDate(date);
                    stock.setUser(user);
                    stock.setNoFaktur(orderID);
                    stock.setStatus(status);

                    stocks.add(stock);
                }else{
                    if(!recipes.isEmpty()){
                        List<Recipe> recipeList = recipeMap.getOrDefault(itemMaster.getItemMasterId(), Collections.emptyList());

                        //This bunch of code is for searching item recipe cogs
                        Set<Long> itemRecipeIds = recipeList.stream()
                                .map(i -> i.getItemMasterRecipe().getItemMasterId()).collect(Collectors.toSet());
                        List<CogsViewDTO> cogsRecipes = cogsService.cogsByItems(new ArrayList<>(itemRecipeIds));
                        Map<Long, Double> cogsRecipeMap = cogsRecipes.stream()
                                .collect(Collectors.toMap(CogsViewDTO::getItemMasterId, CogsViewDTO::getCogs));

                        if(!recipeList.isEmpty()){
                            for(Recipe recipe : recipeList){
                                ItemMaster itemMasterRecipe = recipe.getItemMasterRecipe();
                                if(itemMasterRecipe != null){
                                    // Stock
                                    Stock stock = new Stock();
                                    stock.setLocation(location);
                                    stock.setType(7);
                                    stock.setQty(recipe.getQty() * Double.valueOf(item.getQuantity()));

                                    double cogsRecipe = cogsRecipeMap.get(recipe.getItemMasterRecipe().getItemMasterId());

                                    if(itemMasterRecipe.getUomStockRecipeQty() != null && itemMasterRecipe.getUomStockRecipeQty() > 0){
                                        stock.setPrice(cogsRecipe / recipe.getItemMasterRecipe().getUomStockRecipeQty());
                                    }else{
                                        stock.setPrice(0.0);
                                    }

                                    stock.setTotal(stock.getQty() * stock.getPrice());
                                    stock.setItemMaster(itemMasterRecipe);
                                    stock.setItemCode(itemMasterRecipe.getCode());
                                    stock.setItemBarcode(itemMasterRecipe.getBarcode());
                                    stock.setItemName(itemMasterRecipe.getName());
                                    stock.setUom(itemMasterRecipe.getUomStock());
                                    stock.setUnit(itemMasterRecipe.getUomStock().getUnit());

                                    int inOut = 1;
                                    if(recipe.getQty()>0){
                                        inOut = -1;
                                    }

                                    stock.setInOut(inOut);
                                    stock.setDate(date);
                                    stock.setUser(user);
                                    stock.setNoFaktur(orderID);
                                    stock.setStatus(status);
                                    stock.setTypeItem(0);
                                    stocks.add(stock);
                                }

                            }
                        }

                    }
                }
            }
        }

        // ==== PACKAGING FEE ====
        if(merchantChargeFee > 0){
            GrabPackagingItem grabPackagingItem = grabPackagingItemService.findGrabPackagingItemByLocationIdAndProduct(location.getLocationId(), product).orElse(null);
            if(grabPackagingItem != null){
                if(grabPackagingItem.getItemMaster() != null){
                    ItemMaster packagingItem =  grabPackagingItem.getItemMaster();
                    if (packagingItem.getNeedRecipe() == 0){
                        Stock stock = new Stock();
                        stock.setLocation(location);
                        stock.setType(7);
                        stock.setQty(1.0);
                        stock.setPrice((double) merchantChargeFee);
                        stock.setTotal(stock.getQty() * stock.getPrice());
                        stock.setItemMaster(packagingItem);
                        stock.setItemCode(packagingItem.getCode());
                        stock.setItemBarcode(packagingItem.getBarcode());
                        stock.setItemName(packagingItem.getName());
                        stock.setUom(packagingItem.getUomStock());
                        stock.setUnit(packagingItem.getUomStock().getUnit());
                        stock.setInOut(-1);
                        stock.setDate(date);
                        stock.setUser(user);
                        stock.setNoFaktur(orderID);
                        stock.setStatus(DocumentStatus.RESERVED);

                        stocks.add(stock);
                    }
                }
            }
        }
        // ==== END PACKAGING FEE ====
        if (!stocks.isEmpty()) {
            stocks = stockService.saveAll(stocks);
        }

        try {
            salesGrab.setSalesGrabDetails(salesGrabDetails);
            salesGrabService.save(salesGrab);
        } catch (Exception e) {
            log.warn("Duplicate insert attempt for order {}, ignoring. Error: {}", orderID, e.getMessage());
        }

        log.info("Successfully processed order {}", orderID);
        return stocks;
    }

    public void pushOrderState(PushOrderStateRequestDTO pushOrderStateRequestDTO, ListOrderResponseDTO.Order order, String product) {
        long start;
        String merchantID = pushOrderStateRequestDTO.getMerchantID();
        StrukKasir strukKasir = getStrukKasir(product, merchantID);

        Long locationID = strukKasir.getLocationId();
        Location location = locationService.findById(locationID).orElseThrow(() -> new ResourceNotFoundException("location not found!"));

        StateStatus state = StateStatus.valueOf(pushOrderStateRequestDTO.getState());
        String orderID = pushOrderStateRequestDTO.getOrderID();

        Currency currency = currencyService.findIdrCurrency().orElseThrow(() -> new ResourceNotFoundException("currency IDR has not been setup yet!"));

        log.info("Start push order state");
        if(state.equals(StateStatus.DELIVERED)){
            if (order == null){
                order = getGrabOrder(pushOrderStateRequestDTO, product);
            }

            if (order != null){
                CompanyViewDTO company = companyService.getCompany().orElseThrow(() -> new ResourceNotFoundException("company not found!"));

                start = System.currentTimeMillis();

                log.info("Find cash master web {}", orderID);

                Optional<CashMaster> optCashMaster = getCashMaster(product, locationID);

                CashMaster cashMaster = new CashMaster();
                if (!optCashMaster.isPresent()){
                    int maxCashierNumber = cashMasterService.maxCashierNumber().orElse(0);
                    cashMaster.setCashierNumber(maxCashierNumber + 1);
                    cashMaster.setLocation(location);

                    if(product.equals(Product.GRAB_MART.name())){
                        cashMaster.setType(CashMasterType.GRAB_POS);
                    }else if(product.equals(Product.GRAB_FOOD.name())){
                        cashMaster.setType(CashMasterType.GRAB_FOOD_POS);
                    }

                    cashMaster.setStsLoc("0");

                    cashMaster = cashMasterService.save(cashMaster);
                }else {
                    cashMaster = optCashMaster.get();
                }

                log.info("Time taken to handle find cash master web: {} ms", System.currentTimeMillis() - start);

                start = System.currentTimeMillis();

                log.info("Find system main grabmart sales user id");

                User user = getUser(product);

                LocalDateTime date = getLocalDateTime(order.getCompleteTime(), order.getSubmitTime(), order.getOrderTime());

                start = System.currentTimeMillis();

                log.info("Find cash cashier");
                CashCashier cashCashier = new CashCashier();
                Optional<CashCashier> optCashCashier = cashCashierService.findCashCashier(String.valueOf(user.getUserId()), LocalDate.from(date), locationID, cashMaster.getCashMasterId());
                if (!optCashCashier.isPresent()){
                    cashCashier.setCashMaster(cashMaster);
                    cashCashier.setUser(user);
                    Shift shift = shiftService.findFirstShift().orElseThrow(() -> new ResourceNotFoundException("shift has not been setup yet!"));
                    cashCashier.setShift(shift);
                    cashCashier.setCurrencyOpen(currency);
                    cashCashier.setCurrencyClosing(currency);
                    cashCashier.setDateOpen(date);
                    cashCashier.setRateOpen(1.0);
                    cashCashier.setDateClosing(date);
                    cashCashier.setRateClosing(1.0);
                    cashCashier.setStatus(1);
                }else{
                    cashCashier = optCashCashier.get();
                    cashCashier.setDateClosing(date);
                }
                cashCashier = cashCashierService.save(cashCashier);

                log.info("Time taken to handle find cash cashier: {} ms", System.currentTimeMillis() - start);

                // CHECK IF ORDER NUMBER IS SPLIT
                boolean isOrderSplit = GrabNumberChecker.isOrderSplit(order.getOrderID());

                start = System.currentTimeMillis();

                log.info("Start insert sales");
                Sales sales = new Sales();
                sales.setDate(date);
                sales.setNumber(order.getOrderID());

                log.info("Find Customer");

                new Customer();
                Customer customer;

                String phones = order.getReceiver() != null && order.getReceiver().getPhones() != null ? order.getReceiver().getPhones() : null;
                if(phones != null && !phones.isEmpty()){
                    customer = customerService.findCustomerByCode(phones).orElseGet(() -> customerService.findCustomerPublic().orElseThrow(() -> new ResourceNotFoundException("public customer has not been setup yet!")));
                    sales.setCustomer(customer);
                }else{
                    customer = customerService.findCustomerPublic().orElseThrow(() -> new ResourceNotFoundException("customer public has been not setup yet"));
                    sales.setCustomer(customer);
                }

                log.info("Time taken to handle find customer: {} ms", System.currentTimeMillis() - start);

                sales.setUser(user);
                sales.setCurrency(currency);
                sales.setType(SalesType.CASH);
                sales.setLocation(location);
                sales.setCashCashier(cashCashier);
                sales.setShift(cashCashier.getShift());
                sales.setCashMaster(cashMaster);

                log.info("Sales Detail");

                List<SalesDetail> salesDetails = new ArrayList<>();

                int divisionBy = order.getCurrency().getExponent() != null ? (int) Math.pow(10, order.getCurrency().getExponent()) : 10;

                List<SubmitOrderRequestDTO.Item> items = order.getItems();

                // Sorting list items by total descending
                items.sort((o1, o2) -> Double.compare(o2.getQuantity() * o2.getPrice(),o1.getQuantity() * o1.getPrice()));

                BigDecimal merchantFundPromo = NumberUtils.numberScale(BigDecimal.valueOf(order.getPrice().getMerchantFundPromo() / divisionBy));
                int subTotalTransaction = Optional.of(order.getPrice().getSubtotal()).orElse(0) / divisionBy;
                int merchantChargeFee = Optional.ofNullable(order.getPrice().getMerchantChargeFee()).orElse(0) / divisionBy;

                Set<Long> itemIds = new HashSet<>();
                if(!items.isEmpty()){
                    itemIds = items.stream()
                            .map(item -> Long.valueOf(item.getId())).collect(Collectors.toSet());

                    List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemIds));

                    Map<Long, ItemMaster> itemMasterMap = itemMasters.stream()
                            .collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity(),(existing, replacement) -> existing));

                    List<CogsViewDTO> cogs = cogsService.cogsByItems(new ArrayList<>(itemIds));

                    Map<Long, Double> cogsMap = cogs.stream()
                            .collect(Collectors.toMap(CogsViewDTO::getItemMasterId, CogsViewDTO::getCogs));

                    // ====== CAMPAIGN =====
                    Map<String, CampaignMapDTO> campaignWithItemMap = new HashMap<>();
                    Map<String, CampaignMapDTO> campaignWithoutItemMap = new HashMap<>();
                    Map<String, BigDecimal> totalSalesPerCampaignMap = new HashMap<>();

                    BigDecimal totalSalesCampaign = BigDecimal.ZERO;

                    if(order.getCampaigns() != null && !order.getCampaigns().isEmpty()){
                        for(SubmitOrderRequestDTO.Campaign campaign: order.getCampaigns()){
                            CampaignMapDTO campaignMapDTO = new CampaignMapDTO(campaign.getId(), campaign.getLevel(), campaign.getType(),campaign.getDeductedAmount() / divisionBy, campaign.getMexFundedRatio());

                            BigDecimal totalSalesPerCampaign = BigDecimal.ZERO;

                            if(campaign.getAppliedItemIDs() != null && !campaign.getAppliedItemIDs().isEmpty()){
                                for(String itemId: campaign.getAppliedItemIDs()){
                                    campaignWithItemMap.put(itemId, campaignMapDTO);

                                    SubmitOrderRequestDTO.Item item = items.stream().filter(i -> i.getId().equals(itemId)).findFirst().orElse(null);
                                    if(item != null){
                                        int price = item.getPrice() / divisionBy;
                                        BigDecimal total = NumberUtils.numberScale(BigDecimal.valueOf(Optional.of(price * item.getQuantity()).orElse(0)));

                                        totalSalesPerCampaign = totalSalesPerCampaign.add(total);
                                        totalSalesCampaign = totalSalesCampaign.add(total);
                                    }
                                }
                            }

                            totalSalesPerCampaignMap.put(campaign.getId(), totalSalesPerCampaign);
                            campaignWithoutItemMap.put(campaign.getLevel(), campaignMapDTO);

                        }
                    }
                    // ===== END CAMPAIGN =====

                    BigDecimal remainingMerchantFundPromo = merchantFundPromo;
                    for(SubmitOrderRequestDTO.Item item: items){
                        ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(item.getId()));

                        if (itemMaster != null){
                            SalesDetail salesDetail = new SalesDetail();
                            salesDetail.setSales(sales);

                            salesDetail.setCompanyId(Long.valueOf(itemMaster.getIsBkp()));
                            if(itemMaster.getIsBkp() == 1 && salesDetail.getTax() == 0){
                                salesDetail.setTax(company.getGovernmentVat());
                            }

                            int price = item.getPrice() / divisionBy;
                            salesDetail.setItemMaster(itemMaster);
                            salesDetail.setSellingPrice((double) price);
                            salesDetail.setCurrency(currency);
                            salesDetail.setQty(Double.valueOf(item.getQuantity()));
                            salesDetail.setConvQty(1.0);
                            salesDetail.setUnit(itemMaster.getUomStock());
                            salesDetail.setTotal(NumberUtils.numberScale(BigDecimal.valueOf(Optional.of(price * item.getQuantity()).orElse(0))));

                            BigDecimal ratio;
                            BigDecimal discountGlobal;
                            BigDecimal discountItem = BigDecimal.ZERO;

                            // Get campaign by item id
                            CampaignMapDTO campaignByItem = campaignWithItemMap.get(item.getId());

                            log.info("remaining merchant fund promo {}", remainingMerchantFundPromo);
                            if(remainingMerchantFundPromo.compareTo(BigDecimal.ZERO) > 0){
                                if(campaignByItem != null){
                                    log.info("order is split? {}", isOrderSplit);
                                    if(!isOrderSplit){
                                        BigDecimal totalSalesPerCampaign = totalSalesPerCampaignMap.get(campaignByItem.getId());

                                        log.info("order {} total sales per campaign {} ", order.getOrderID(), totalSalesPerCampaign);
                                        if(totalSalesPerCampaign != null){
                                            ratio = salesDetail.getTotal().divide(totalSalesPerCampaign,14, RoundingMode.HALF_UP);

                                            log.info("sales detail total {}", salesDetail.getTotal());
                                            log.info("ratio {}", NumberUtils.numberScale(ratio));

                                            log.info("campaign deducted amount {}", campaignByItem.getDeductedAmount());
                                            log.info("campaign mex funded ration {}", campaignByItem.getMexFundedRatio());

                                            BigDecimal deductedAmount = BigDecimal.valueOf(campaignByItem.getDeductedAmount());
                                            BigDecimal ratioDeductedAmount = BigDecimal.valueOf(campaignByItem.getMexFundedRatio()).divide(BigDecimal.valueOf(100), 14, RoundingMode.HALF_UP);
                                            BigDecimal totalDeductedAmount = deductedAmount.multiply(ratioDeductedAmount);

                                            log.info("campaign total deducted amount {}", totalDeductedAmount);

                                            discountItem = NumberUtils.numberScale(totalDeductedAmount.multiply(ratio));

                                            log.info("discount item {}", discountItem);
                                            remainingMerchantFundPromo = remainingMerchantFundPromo.subtract(discountItem);
                                        }
                                    }else{
                                        ratio = salesDetail.getTotal().divide(totalSalesCampaign,14, RoundingMode.HALF_UP);
                                        discountItem = NumberUtils.numberScale(merchantFundPromo.multiply(ratio));
                                        remainingMerchantFundPromo = remainingMerchantFundPromo.subtract(discountItem);
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
                                }
                            }

                            BigDecimal discountAmount = NumberUtils.numberScale(Optional.of(salesDetail.getDiscountItem().add(salesDetail.getDiscountGlobal())).orElse(BigDecimal.valueOf(0)));
                            salesDetail.setDiscountAmount(discountAmount);
                            salesDetail.setTotal(salesDetail.getTotal().subtract(salesDetail.getDiscountAmount()));

                            log.info("End Campaign");

                            double newCogs = 0.0;
                            if(itemMaster.getTypeItem() == TypeItem.BELI_PUTUS.ordinal()){
                                newCogs = Optional.ofNullable(cogsMap.get(Long.valueOf(item.getId()))).orElse(0.0);
                            }else if(itemMaster.getTypeItem() == TypeItem.KONSINYASI.ordinal()){
                                newCogs = cogsService.getLastCogsConsigment(itemMaster, salesDetail, company);
                            }
                            salesDetail.setCogs(newCogs);

                            salesDetails.add(salesDetail);
                        }
                    }

                    // remaining ratio for discount
                    if(!salesDetails.isEmpty()){
                        for(SalesDetail salesDetail: salesDetails){
                            CampaignMapDTO campaignByItem = campaignWithItemMap.get(String.valueOf(salesDetail.getItemMaster().getItemMasterId()));

                            log.info("remaining merchant fund promo {}", remainingMerchantFundPromo);
                            if(remainingMerchantFundPromo.compareTo(BigDecimal.ZERO) > 0 && campaignByItem == null && salesDetail.getDiscountAmount().compareTo(BigDecimal.ZERO) <= 0){
                                if (remainingMerchantFundPromo.compareTo(BigDecimal.valueOf(1000)) < 0) {
                                    BigDecimal discountGlobal = remainingMerchantFundPromo;

                                    remainingMerchantFundPromo = remainingMerchantFundPromo.subtract(discountGlobal);

                                    salesDetail.setDiscountGlobal(discountGlobal);
                                } else {
                                    BigDecimal ratio = salesDetail.getTotal().divide(BigDecimal.valueOf(subTotalTransaction), 14, RoundingMode.HALF_UP);

                                    log.info("ratio {}", NumberUtils.numberScale(ratio));

                                    BigDecimal discountGlobal = NumberUtils.numberScale(merchantFundPromo.multiply(ratio));
                                    remainingMerchantFundPromo = remainingMerchantFundPromo.subtract(discountGlobal);

                                    salesDetail.setDiscountGlobal(discountGlobal);
                                }

                                BigDecimal discountAmount = NumberUtils.numberScale(Optional.of(salesDetail.getDiscountItem().add(salesDetail.getDiscountGlobal())).orElse(BigDecimal.valueOf(0)));
                                salesDetail.setDiscountAmount(discountAmount);
                                salesDetail.setTotal(salesDetail.getTotal().subtract(salesDetail.getDiscountAmount()));
                            }
                        }

                        // ==== PACKAGING FEE ====
                        if(merchantChargeFee > 0){
                            GrabPackagingItem grabPackagingItem = grabPackagingItemService.findGrabPackagingItemByLocationIdAndProduct(location.getLocationId(), product).orElse(null);

                            if(grabPackagingItem != null){
                                ItemMaster packagingItem =  grabPackagingItem.getItemMaster();
                                Sales finalSales = sales;

                                if(packagingItem != null){
                                    SalesDetail salesDetail = new SalesDetail();
                                    salesDetail.setSales(finalSales);
                                    salesDetail.setItemMaster(packagingItem);

                                    CogsViewDTO cogsView = cogsService.cogsByItem(packagingItem.getItemMasterId());

                                    double newCogs = 0.0;
                                    if(packagingItem.getTypeItem() == TypeItem.BELI_PUTUS.ordinal()){
                                        newCogs = cogsView.getCogs();
                                    }else if(packagingItem.getTypeItem() == TypeItem.KONSINYASI.ordinal()){
                                        newCogs = cogsService.getLastCogsConsigment(packagingItem, salesDetail, company);
                                    }
                                    salesDetail.setCogs(newCogs);
                                    salesDetail.setSellingPrice((double) merchantChargeFee);
                                    salesDetail.setCurrency(currency);
                                    salesDetail.setQty(1.0);
                                    salesDetail.setConvQty(1.0);
                                    salesDetail.setUnit(packagingItem.getUomStock());
                                    salesDetail.setTotal(BigDecimal.valueOf(salesDetail.getQty() * salesDetail.getSellingPrice()));

                                    salesDetail.setCompanyId(Long.valueOf(packagingItem.getIsBkp()));
                                    if(packagingItem.getIsBkp() == 1 && salesDetail.getTax() == 0){
                                        salesDetail.setTax(company.getGovernmentVat());
                                    }

                                    salesDetails.add(salesDetail);
                                }
                            }
                        }
                        // ==== END PACKAGING FEE ====

                        BigDecimal grandTotal = salesDetails.stream().map(SalesDetail::getTotal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal totalFromGrab = NumberUtils.numberScale(BigDecimal.valueOf(order.getPrice().getTotal() / divisionBy));

                        BigDecimal remainingTotal = totalFromGrab.subtract(grandTotal);
                        if(remainingTotal.compareTo(BigDecimal.ZERO) > 0){
                            SalesDetail lastDetail = salesDetails.get(salesDetails.size() - 1);
                            lastDetail.setDiscountGlobal(lastDetail.getDiscountGlobal().add(remainingTotal));
                            lastDetail.setDiscountAmount(lastDetail.getDiscountGlobal().add(lastDetail.getDiscountItem()));
                            lastDetail.setTotal(lastDetail.getTotal().subtract(lastDetail.getDiscountAmount()));
                        }
                    }

                    if (!salesDetails.isEmpty()) sales.setSalesDetails(salesDetails);
                }

                BigDecimal subTotal = sales.getSalesDetails().stream()
                        .map(s -> BigDecimal.valueOf(s.getQty()).multiply(BigDecimal.valueOf(s.getSellingPrice())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal grandTotal = sales.getSalesDetails().stream().map(SalesDetail::getTotal).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                sales.setAmount(grandTotal.doubleValue());
                sales = salesService.save(sales);

                log.info("Time taken to handle sales: {} ms", System.currentTimeMillis() - start);

                log.info("End Sales");

                log.info("Find system main grabmart merchant");
                start = System.currentTimeMillis();

                // ==== PAYMENT ====
                Optional<SystemMain> optSystemMain;

                Optional<Merchant> optMerchant = Optional.empty();
                Optional<Merchant> optMerchantPackaging = Optional.empty();

                if(product.equals(Product.GRAB_MART.name())){
                    optSystemMain = systemMainService.findSystemPropertyName("GRABMART_MERCHANT_NAME");
                    String merchantName = optSystemMain.map(systemMain -> String.valueOf(systemMain.getValueprop())).orElse("");
                    optMerchant = merchantService.findMerchantByLocationAndDescription(merchantName, location.getLocationId());

                    optSystemMain = systemMainService.findSystemPropertyName("GRABMART_MERCHANT_PACKAGING_NAME");
                    String merchantPackagingName = optSystemMain.map(systemMain -> String.valueOf(systemMain.getValueprop())).orElse("");
                    optMerchantPackaging = merchantService.findMerchantByLocationAndDescription(merchantPackagingName, location.getLocationId());
                } else if (product.equals(Product.GRAB_FOOD.name())) {
                    optSystemMain = systemMainService.findSystemPropertyName("GRABFOOD_MERCHANT_NAME");
                    String merchantName = optSystemMain.map(systemMain -> String.valueOf(systemMain.getValueprop())).orElse("");
                    optMerchant = merchantService.findMerchantByLocationAndDescription(merchantName, location.getLocationId());

                    optSystemMain = systemMainService.findSystemPropertyName("GRABFOOD_MERCHANT_PACKAGING_NAME");
                    String merchantPackagingName = optSystemMain.map(systemMain -> String.valueOf(systemMain.getValueprop())).orElse("");
                    optMerchantPackaging = merchantService.findMerchantByLocationAndDescription(merchantPackagingName, location.getLocationId());
                }


                Payment payment = new Payment();
                payment.setSales(sales);
                payment.setCurrency(currency);
                payment.setPayDate(date);

                double paymentAmount = (double) order.getPrice().getTotal() / divisionBy;

                if(optMerchantPackaging.isPresent() && merchantChargeFee > 0){
                    paymentAmount -= merchantChargeFee;
                }
                payment.setAmount(paymentAmount);
                payment.setRate(1.0);

                if (optMerchant.isPresent()){
                    Merchant merchant = optMerchant.get();
                    payment.setMerchant(merchant);

                    payment.setBank(merchant.getBank());
                    payment.setPaymentMethod(merchant.getPaymentMethod());
                }
                paymentService.save(payment);

                // === PAYMENT PACKAGING FEE ===
                if(optMerchantPackaging.isPresent() && merchantChargeFee > 0){
                    Merchant merchantPackaging = optMerchantPackaging.get();

                    payment = new Payment();
                    payment.setSales(sales);
                    payment.setCurrency(currency);
                    payment.setPayDate(date);

                    paymentAmount = merchantChargeFee;
                    payment.setAmount(paymentAmount);
                    payment.setRate(1.0);

                    payment.setMerchant(merchantPackaging);
                    payment.setBank(merchantPackaging.getBank());
                    payment.setPaymentMethod(merchantPackaging.getPaymentMethod());

                    paymentService.save(payment);
                }
                // === END PAYMENT PACKAGING FEE ===

                ReturnPayment returnPayment = new ReturnPayment();
                returnPayment.setSales(sales);
                returnPayment.setCurrency(currency);

                returnPaymentService.save(returnPayment);
                // ==== END PAYMENT ====

                log.info("Time taken to handle find system main grabmart merchant: {} ms", System.currentTimeMillis() - start);

                start = System.currentTimeMillis();

                int countSales = salesService.countSalesByCashCashierId(cashCashier.getCashCashierId()).orElse(0);
                int countSalesDetail = salesService.countSalesDetailByCashCashierId(cashCashier.getCashCashierId()).orElse(0);
                int countPayment = paymentService.countPaymentByCashCashierId(cashCashier.getCashCashierId()).orElse(0);
                cashCashier.setCountSales(countSales);
                cashCashier.setCountDetail(countSalesDetail);
                cashCashier.setCountPayment(countPayment);
                cashCashierService.save(cashCashier);

                // delete and insert stock for real data stock
                List<Stock> stocks = new ArrayList<>();
                salesGrabDetailService.deleteSalesGrabDetailBySalesGrabNumber(orderID);
                salesGrabService.deleteSalesGrabByNumber(orderID);
                stockService.deleteStockByNoFaktur(orderID);

                List<Recipe> recipes = recipeService.findByItemMasterIds(new  ArrayList<>(itemIds));
                Map<Long, List<Recipe>> recipeMap = recipes.stream().collect(Collectors.groupingBy(r -> r.getItemMaster().getItemMasterId()));

                // ======== Sales Grab ========
                SalesGrab salesGrab = new SalesGrab();
                salesGrab.setNumber(orderID);
                salesGrab.setDate(date);
                salesGrab.setStatus(DocumentStatus.DRAFT);
                salesGrab.setCustomer(customer);
                salesGrab.setLocation(location);
                salesGrab.setAmount(subTotal.doubleValue());
                List<SalesGrabDetail> salesGrabDetails = new ArrayList<>();

                for (SalesDetail salesDetail : sales.getSalesDetails()) {
                        ItemMaster itemMaster = salesDetail.getItemMaster();
                        // Sales Grab Detail
                        SalesGrabDetail salesGrabDetail = new SalesGrabDetail();
                        salesGrabDetail.setSalesGrab(salesGrab);
                        salesGrabDetail.setItemMaster(itemMaster);
                        salesGrabDetail.setSellingPrice(salesDetail.getSellingPrice());
                        salesGrabDetail.setQty(salesDetail.getQty());
                        salesGrabDetail.setTotal(BigDecimal.valueOf(salesDetail.getQty() * salesDetail.getSellingPrice()));
                        salesGrabDetails.add(salesGrabDetail);

                        // Stock
                        if(itemMaster.getNeedRecipe() == 0){
                            log.info("Create stock for item {}", itemMaster.getName());
                            Stock stock = new Stock();
                            stock.setLocation(location);
                            stock.setType(7);
                            stock.setQty(salesDetail.getQty());
                            stock.setPrice(salesDetail.getSellingPrice());
                            stock.setTotal(salesDetail.getQty() * salesDetail.getSellingPrice());
                            stock.setItemMaster(itemMaster);
                            stock.setItemCode(itemMaster.getCode());
                            stock.setItemBarcode(itemMaster.getBarcode());
                            stock.setItemName(itemMaster.getName());
                            stock.setUom(itemMaster.getUomStock());
                            stock.setUnit(itemMaster.getUomStock().getUnit());
                            stock.setInOut(-1);
                            stock.setDate(date);
                            stock.setUser(user);
                            stock.setNoFaktur(sales.getNumber());
                            stock.setSalesDetail(salesDetail);
                            stock.setOpnameId(sales.getSalesId());
                            stock.setStatus(DocumentStatus.APPROVED);

                            stocks.add(stock);
                        }else{
                            if(!recipes.isEmpty()){
                                List<Recipe> recipeList = recipeMap.getOrDefault(itemMaster.getItemMasterId(), Collections.emptyList());

                                //This bunch of code is for searching item recipe cogs
                                Set<Long> itemRecipeIds = recipeList.stream()
                                        .map(i -> i.getItemMasterRecipe().getItemMasterId()).collect(Collectors.toSet());
                                List<CogsViewDTO> cogsRecipes = cogsService.cogsByItems(new ArrayList<>(itemRecipeIds));
                                Map<Long, Double> cogsRecipeMap = cogsRecipes.stream()
                                        .collect(Collectors.toMap(CogsViewDTO::getItemMasterId, CogsViewDTO::getCogs));

                                double totalCogs = 0.0;
                                if(!recipeList.isEmpty()){
                                    for(Recipe recipe : recipeList){
                                        ItemMaster itemMasterRecipe = recipe.getItemMasterRecipe();
                                        if(itemMasterRecipe != null){
                                            if(itemMasterRecipe.getNeedRecipe() == 0){
                                                log.info("Create stock for item {}", itemMasterRecipe.getName());
                                                // Stock
                                                Stock stock = new Stock();
                                                stock.setLocation(location);
                                                stock.setType(7);
                                                stock.setQty(recipe.getQty() * salesDetail.getQty());

                                                double cogsRecipe = cogsRecipeMap.get(recipe.getItemMasterRecipe().getItemMasterId());
                                                totalCogs += cogsRecipe * recipe.getQty();

                                                if(itemMasterRecipe.getUomStockRecipeQty() != null && itemMasterRecipe.getUomStockRecipeQty() > 0){
                                                    stock.setPrice(cogsRecipe / recipe.getItemMasterRecipe().getUomStockRecipeQty());
                                                }else{
                                                    stock.setPrice(0.0);
                                                }

                                                stock.setTotal(stock.getQty() * stock.getPrice());
                                                stock.setItemMaster(itemMasterRecipe);
                                                stock.setItemCode(itemMasterRecipe.getCode());
                                                stock.setItemBarcode(itemMasterRecipe.getBarcode());
                                                stock.setItemName(itemMasterRecipe.getName());
                                                stock.setUom(itemMasterRecipe.getUomStock());
                                                stock.setUnit(itemMasterRecipe.getUomStock().getUnit());

                                                int inOut = 1;
                                                if(recipe.getQty()>0){
                                                    inOut = -1;
                                                }
                                                stock.setInOut(inOut);
                                                stock.setDate(date);
                                                stock.setUser(user);
                                                stock.setNoFaktur(sales.getNumber());
                                                stock.setStatus(DocumentStatus.APPROVED);
                                                stock.setOpnameId(sales.getSalesId());
                                                stock.setSalesDetail(salesDetail);
                                                stock.setTypeItem(0);
                                                stocks.add(stock);
                                            }

                                        }
                                    }
                                    salesDetail.setBom(1);
                                    salesDetail.setCogs(totalCogs);
                                }
                            }
                        }
                }

                log.info("TOTAL STOCK INSERT {}", stocks.size());
                stockService.saveAll(stocks);
                try {
                    salesGrab.setSalesGrabDetails(salesGrabDetails);
                    salesGrabService.save(salesGrab);
                } catch (Exception e) {
                    log.warn("Duplicate insert attempt for order {}, ignoring. Error: {}", orderID, e.getMessage());
                }

                log.info("Time taken to handle sales: {} ms", System.currentTimeMillis() - start);
            }
        }else if(state.equals(StateStatus.CANCELLED) || state.equals(StateStatus.FAILED)){
            if(order == null){
                order = getGrabOrder(pushOrderStateRequestDTO, product);
            }

            if (order != null){
                stockService.deleteStockByNoFaktur(order.getOrderID());
                salesGrabService.findSalesGrabByNumber(order.getOrderID()).ifPresent(salesGrabService::deleteSalesGrab);
                log.info("Delete stock is success");
            }
        }

        log.info("End push order state {}",pushOrderStateRequestDTO.getOrderID());
    }

    public<T extends GrabOAuthService, U extends GrabOrderSyncService> ResponseEntity<?> manualUploadPerSalesForApi(T grabOAuthService, U grabOrderSyncService, String number, Long locationId, String product) {
        if(number == null || number.isEmpty()) throw new ResourceNotFoundException("sales number cannot be empty!");
        if(locationId == null) throw new ResourceNotFoundException("location not found!");

        Location location = locationService.findById(locationId).orElseThrow(() -> new ResourceNotFoundException("location not found!"));
        StrukKasir strukKasir = strukKasirService.findStrukKasirByLocationId(location.getLocationId()).orElseThrow(() -> new ResourceNotFoundException("merchant has not been setup yet!"));
        String token = grabOAuthService.getGrabToken();

        List<String> orderIDs = Collections.singletonList(number);

        String merchantId = "";
        if(product.equals(Product.GRAB_MART.name())){
            merchantId = strukKasir.getGrabMerchantId();
        } else if (product.equals(Product.GRAB_FOOD.name())) {
            merchantId = strukKasir.getGrabFoodMerchantId();
        }

        ListOrderResponseDTO listOrderResponseDTO = grabOrderSyncService.getListOrder(token, merchantId,null,null,orderIDs);

        if(listOrderResponseDTO != null && listOrderResponseDTO.getOrders() != null){
            ListOrderResponseDTO.Order order = listOrderResponseDTO.getOrders().get(0);
            if(!order.getOrderState().equals(StateStatus.DELIVERED.name())) throw new ResourceNotFoundException("upload sales is failed!, only the DELIVERED status can be uploaded");
                PushOrderStateRequestDTO pushOrderStateRequestDTO = new PushOrderStateRequestDTO();
                pushOrderStateRequestDTO.setMerchantID(order.getMerchantID());
                pushOrderStateRequestDTO.setPartnerMerchantID(order.getPartnerMerchantID());
                pushOrderStateRequestDTO.setOrderID(order.getOrderID());
                pushOrderStateRequestDTO.setState(order.getOrderState());
                pushOrderStateRequestDTO.setCode("COMPLETED");

                pushOrderState(pushOrderStateRequestDTO, order, product);

            }

        SuccessResponse<String> response = new SuccessResponse<>("success", null);
        return ResponseEntity.ok(response);
    }

    public void automaticUploadSales(ListOrderResponseDTO.Order order, String product) {
        if (order.getOrderState().equals(StateStatus.DELIVERED.name())){
            PushOrderStateRequestDTO pushOrderStateRequestDTO = new PushOrderStateRequestDTO();
            pushOrderStateRequestDTO.setMerchantID(order.getMerchantID());
            pushOrderStateRequestDTO.setPartnerMerchantID(order.getPartnerMerchantID());
            pushOrderStateRequestDTO.setOrderID(order.getOrderID());
            pushOrderStateRequestDTO.setState(order.getOrderState());
            pushOrderStateRequestDTO.setCode("COMPLETED");

            pushOrderState(pushOrderStateRequestDTO, order, product);
        }

    }
}

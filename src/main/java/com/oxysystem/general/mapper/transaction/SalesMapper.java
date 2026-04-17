package com.oxysystem.general.mapper.transaction;

import com.oxysystem.general.dto.general.company.view.CompanyViewDTO;
import com.oxysystem.general.dto.posmaster.cogs.view.CogsViewDTO;
import com.oxysystem.general.dto.posmaster.payment.data.PaymentDTO;
import com.oxysystem.general.dto.transaction.salesTaking.data.SalesTakingDTO;
import com.oxysystem.general.enums.*;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.model.tenant.general.*;
import com.oxysystem.general.model.tenant.general.Currency;
import com.oxysystem.general.model.tenant.posmaster.*;
import com.oxysystem.general.model.tenant.transaction.SalesTaking;
import com.oxysystem.general.model.tenant.transaction.SalesTakingDetail;
import com.oxysystem.general.model.tenant.transaction.sales.Sales;
import com.oxysystem.general.model.tenant.transaction.sales.SalesDetail;
import com.oxysystem.general.model.tenant.transaction.stock.Stock;
import com.oxysystem.general.service.admin.UserService;
import com.oxysystem.general.service.general.*;
import com.oxysystem.general.service.posmaster.*;
import com.oxysystem.general.service.transaction.sales.SalesDetailService;
import com.oxysystem.general.service.transaction.sales.SalesService;
import com.oxysystem.general.service.transaction.stock.StockService;
import com.oxysystem.general.util.NumberUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SalesMapper {
    private final SalesService salesService;
    private final SalesDetailService salesDetailService;
    private final UserService userService;
    private final CashMasterService cashMasterService;
    private final CashCashierService cashCashierService;
    private final ShiftService shiftService;
    private final CurrencyService currencyService;
    private final CompanyService companyService;
    private final CogsService cogsService;
    private final StockService stockService;
    private final PaymentMethodService paymentMethodService;
    private final MerchantService merchantService;
    private final BankService bankService;
    private final PaymentService paymentService;
    private final ReturnPaymentService returnPaymentService;
    private final UnitService unitService;
    private final RecipeService recipeService;

    public Sales mappingFromSalesTaking(SalesTaking salesTaking, SalesTakingDTO salesTakingDTO) {
        Location location = salesTaking.getLocation();
        User user = userService.findById(salesTaking.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Currency currency = currencyService.findIdrCurrency().orElseThrow(() -> new ResourceNotFoundException("Currency not found"));

        //CASH MASTER
        Optional<CashMaster> optCashMaster = cashMasterService.findCashMasterByType(location.getLocationId(), CashMasterType.WEB_POS.ordinal());
        CashMaster cashMaster = new CashMaster();
        if (!optCashMaster.isPresent()){
            int maxCashierNumber = cashMasterService.maxCashierNumber().orElse(0);
            cashMaster.setCashierNumber(maxCashierNumber + 1);
            cashMaster.setLocation(location);
            cashMaster.setType(CashMasterType.WEB_POS);
            cashMaster.setStsLoc("0");

            cashMaster = cashMasterService.save(cashMaster);
        }else {
            cashMaster = optCashMaster.get();
        }

        LocalDateTime date = salesTaking.getDate();

        //CASH CASHIER
        CashCashier cashCashier = new CashCashier();
        Optional<CashCashier> optCashCashier = cashCashierService.findCashCashier(String.valueOf(user.getUserId()), LocalDate.from(date), location.getLocationId(), cashMaster.getCashMasterId());
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

        //SALES
        Sales sales = new Sales();
        // Create number
        sales.setNumberPrefix(salesTaking.getNumberPrefix());
        sales.setCounter(salesTaking.getCounter());
        sales.setNumber(salesTaking.getNumber());
        sales.setDate(date);
        // End create number
        sales.setCustomer(salesTaking.getCustomer());
        sales.setUser(user);
        sales.setCurrency(currency);
        sales.setType(salesTaking.getType());
        sales.setLocation(location);
        sales.setCashCashier(cashCashier);
        sales.setShift(cashCashier.getShift());
        sales.setCashMaster(cashMaster);
        sales = salesService.save(sales);

        //Sales Detail
        List<SalesDetail> salesDetails = new ArrayList<>();
        CompanyViewDTO company = companyService.getCompany().orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        //This bunch of code is for searching cogs by list of item
        Set<Long> itemIds = salesTaking.getSalesTakingDetailList().stream()
                .map(item -> item.getItemMaster().getItemMasterId()).collect(Collectors.toSet());
        List<CogsViewDTO> cogs = cogsService.cogsByItems(new ArrayList<>(itemIds));
        Map<Long, Double> cogsMap = cogs.stream()
                .collect(Collectors.toMap(CogsViewDTO::getItemMasterId, CogsViewDTO::getCogs));

        //This bunch of code is for searching unit by list of uom id
        Set<Long> uomIds = salesTaking.getSalesTakingDetailList().stream()
                .map(SalesTakingDetail::getUomId).collect(Collectors.toSet());
        List<Unit> units = unitService.findAllByUomIds(new ArrayList<>(uomIds));
        Map<Long, Unit> unitMap = units.stream().collect(Collectors.toMap(Unit::getUomId, Function.identity()));

        //This bunch of code is for searching item recipe
        List<Recipe> recipes = recipeService.findByItemMasterIds(new  ArrayList<>(itemIds));
        Map<Long, List<Recipe>> recipeMap = recipes.stream().collect(Collectors.groupingBy(r -> r.getItemMaster().getItemMasterId()));

        BigDecimal totalAmount = BigDecimal.ZERO;
        for(SalesTakingDetail salesTakingDetail: salesTaking.getSalesTakingDetailList()){
            SalesDetail salesDetail = new SalesDetail();
            ItemMaster itemMaster = salesTakingDetail.getItemMaster();

            salesDetail.setSales(sales);

            salesDetail.setCompanyId((long) itemMaster.getIsBkp());
            if(itemMaster.getIsBkp() == 1 && salesDetail.getTax() == 0){
                salesDetail.setTax(company.getGovernmentVat());
            }

            salesDetail.setItemMaster(itemMaster);
            salesDetail.setSellingPrice(salesTakingDetail.getSellingPrice());
            salesDetail.setCurrency(currency);
            salesDetail.setQty(salesTakingDetail.getQty());
            salesDetail.setConvQty(salesTakingDetail.getConvQty());

            Unit unit = unitMap.get(salesTakingDetail.getUomId());
            salesDetail.setUnit(unit);
            salesDetail.setDiscountItem(NumberUtils.numberScale(BigDecimal.valueOf(salesTakingDetail.getDiscountItem())));
            salesDetail.setDiscountAmount(NumberUtils.numberScale(BigDecimal.valueOf(salesTakingDetail.getDiscountAmount())));

            BigDecimal subTotal = NumberUtils.numberScale(BigDecimal.valueOf(Optional.of(salesDetail.getSellingPrice() * salesDetail.getQty()).orElse(0.0)));
            salesDetail.setTotal(subTotal.subtract(salesDetail.getDiscountAmount()));

            totalAmount = totalAmount.add(salesDetail.getTotal());

            double newCogs = 0.0;
            if(itemMaster.getTypeItem() == TypeItem.BELI_PUTUS.ordinal()){
                newCogs = Optional.ofNullable(cogsMap.get(salesDetail.getItemMaster().getItemMasterId())).orElse(0.0);
            }else if(itemMaster.getTypeItem() == TypeItem.KONSINYASI.ordinal()){
                newCogs = cogsService.getLastCogsConsigment(itemMaster, salesDetail, company);
            }
            salesDetail.setCogs(newCogs);
            salesDetail = salesDetailService.save(salesDetail);



            if(itemMaster.getNeedRecipe() == 0){
                // Stock
                Stock stock = new Stock();
                stock.setLocation(location);
                stock.setType(7);
                stock.setQty(salesDetail.getQty() * salesDetail.getConvQty());
                stock.setPrice(salesDetail.getCogs() / salesDetail.getConvQty());
                stock.setTotal(stock.getQty() * stock.getPrice());
                stock.setItemMaster(itemMaster);
                stock.setItemCode(itemMaster.getCode());
                stock.setItemBarcode(itemMaster.getBarcode());
                stock.setItemName(itemMaster.getName());
                stock.setUom(unit);
                stock.setUnit(unit.getUnit());

                int inOut = -1;
                if(sales.getType().equals(SalesType.RETUR_CASH)){
                    inOut = 1;
                }
                stock.setInOut(inOut);
                stock.setDate(date);
                stock.setUser(user);
                stock.setNoFaktur(sales.getNumber());
                stock.setStatus(DocumentStatus.APPROVED);
                stock.setOpnameId(sales.getSalesId());
                stock.setSalesDetail(salesDetail);
                stockService.save(stock);
            }else{
                if(!recipes.isEmpty()){
                    List<Stock> stocks = new ArrayList<>();
                    List<Recipe> recipeList = recipeMap.getOrDefault(itemMaster.getItemMasterId(), Collections.emptyList());


                    //This bunch of code is for searching item recipe cogs
                    Set<Long> itemRecipeIds = recipeList.stream()
                            .map(item -> item.getItemMasterRecipe().getItemMasterId()).collect(Collectors.toSet());
                    List<CogsViewDTO> cogsRecipes = cogsService.cogsByItems(new ArrayList<>(itemRecipeIds));
                    Map<Long, Double> cogsRecipeMap = cogsRecipes.stream()
                            .collect(Collectors.toMap(CogsViewDTO::getItemMasterId, CogsViewDTO::getCogs));

                    double totalCogs = 0.0;
                    if(!recipeList.isEmpty()){
                        for(Recipe recipe : recipeList){
                            ItemMaster itemMasterRecipe = recipe.getItemMasterRecipe();
                            if(itemMasterRecipe != null){
                                if(itemMasterRecipe.getNeedRecipe() == 0){
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
                                    if(sales.getType() == SalesType.RETUR_CASH || sales.getType() == SalesType.RETUR_CREDIT){
                                        if(recipe.getQty()<0){
                                            inOut = -1;
                                        }
                                    }else{
                                        if(recipe.getQty()>0){
                                            inOut = -1;
                                        }
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
                        stockService.saveAll(stocks);
                    }

                }
            }
            salesDetails.add(salesDetail);
        }

        sales.setSalesDetails(salesDetails);
        sales.setAmount(totalAmount.doubleValue());

        //PAYMENT
        PaymentDTO paymentDTO = salesTakingDTO.getPayment();

        Payment payment = new Payment();
        payment.setSales(sales);
        payment.setCurrency(currency);
        payment.setPayDate(date);

        double paymentAmount = salesTakingDTO.getPayment().getAmount();
        payment.setAmount(paymentAmount);
        payment.setRate(1.0);

        if(paymentDTO.getPaymentMethodId() != null){
            PaymentMethod paymentMethod = paymentMethodService.findById(paymentDTO.getPaymentMethodId()).orElseThrow(() -> new ResourceNotFoundException("Payment Method not found!"));
            payment.setPaymentMethod(paymentMethod);
        }

        if(paymentDTO.getMerchantId() != null){
            Merchant merchant = merchantService.findById(paymentDTO.getMerchantId()).orElseThrow(() -> new ResourceNotFoundException("Merchant not found!"));
            payment.setMerchant(merchant);

            if(merchant != null){
                payment.setBank(merchant.getBank());
                payment.setPaymentMethod(merchant.getPaymentMethod());

                if(merchant.getPaymentBy() == MerchantPaymentBy.CUSTOMER.ordinal()){
                    payment.setCostCardPercent(merchant.getPersenExpense());
                    double costCardAmount = (merchant.getPersenExpense() / 100) * totalAmount.doubleValue();
                    payment.setCostCardAmount(costCardAmount);
                    payment.setAmount(payment.getAmount() -  costCardAmount);
                }
            }
        }

        if(paymentDTO.getBankId() != null){
            Bank bank = bankService.findById(paymentDTO.getBankId()).orElseThrow(() -> new ResourceNotFoundException("Bank not found!"));
            payment.setBank(bank);
        }

        paymentService.save(payment);

        ReturnPayment returnPayment = new ReturnPayment();
        returnPayment.setSales(sales);
        returnPayment.setCurrency(currency);
        returnPayment.setAmount(payment.getAmount() - totalAmount.doubleValue());

        returnPaymentService.save(returnPayment);

        if(sales.getType().equals(SalesType.RETUR_CASH) || sales.getType().equals(SalesType.RETUR_CREDIT)){
            payment.setAmount(payment.getAmount() * -1);
        }

        int countSales = salesService.countSalesByCashCashierId(cashCashier.getCashCashierId()).orElse(0);
        int countSalesDetail = salesService.countSalesDetailByCashCashierId(cashCashier.getCashCashierId()).orElse(0);
        int countPayment = paymentService.countPaymentByCashCashierId(cashCashier.getCashCashierId()).orElse(0);
        cashCashier.setCountSales(countSales);
        cashCashier.setCountDetail(countSalesDetail);
        cashCashier.setCountPayment(countPayment);
        cashCashierService.save(cashCashier);

        return sales;
    }
}

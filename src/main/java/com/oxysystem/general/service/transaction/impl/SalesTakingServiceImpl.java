package com.oxysystem.general.service.transaction.impl;

import com.oxysystem.general.dto.general.children.data.ChildrenDTO;
import com.oxysystem.general.dto.transaction.salesTaking.data.SalesTakingDTO;
import com.oxysystem.general.dto.transaction.salesTaking.view.SalesTakingPlaygroundViewDTO;
import com.oxysystem.general.dto.transaction.salesTakingDetail.data.SalesTakingDetailDTO;
import com.oxysystem.general.dto.transaction.salesTakingDetail.view.SalesTakingDetailQtyReturnDTO;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.enums.SalesType;
import com.oxysystem.general.exception.ResourceConflictException;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.mapper.transaction.SalesMapper;
import com.oxysystem.general.model.db1.BaseEntity;
import com.oxysystem.general.model.db1.admin.User;
import com.oxysystem.general.model.db1.general.Customer;
import com.oxysystem.general.model.db1.general.Location;
import com.oxysystem.general.model.db1.general.PlaygroundRegistration;
import com.oxysystem.general.model.db1.posmaster.ItemMaster;
import com.oxysystem.general.model.db1.posmaster.Unit;
import com.oxysystem.general.model.db1.system.SystemMain;
import com.oxysystem.general.model.db1.transaction.SalesTaking;
import com.oxysystem.general.model.db1.transaction.SalesTakingDetail;
import com.oxysystem.general.repository.db1.transaction.SalesTakingRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.CustomerService;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import com.oxysystem.general.service.system.SystemMainService;
import com.oxysystem.general.service.transaction.SalesTakingService;
import com.oxysystem.general.service.transaction.sales.SalesService;
import com.oxysystem.general.util.DateUtils;
import com.oxysystem.general.util.ResponseUtils;
import com.oxysystem.general.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalesTakingServiceImpl implements SalesTakingService {
    private final SalesTakingRepository salesTakingRepository;
    private final CustomerService customerService;
    private final SystemMainService systemMainService;
    private final LocationService locationService;
    private final ItemMasterService itemMasterService;
    private final SalesMapper salesMapper;

    public SalesTakingServiceImpl(SalesTakingRepository salesTakingRepository, CustomerService customerService, SystemMainService systemMainService, LocationService locationService, ItemMasterService itemMasterService,SalesMapper salesMapper) {
        this.salesTakingRepository = salesTakingRepository;
        this.customerService = customerService;
        this.systemMainService = systemMainService;
        this.locationService = locationService;
        this.itemMasterService = itemMasterService;
        this.salesMapper = salesMapper;
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public SalesTaking saveForPlayground(PlaygroundRegistration playgroundRegistration, Map<Long, ItemMaster> itemMasterMap, List<ChildrenDTO> childrenDTOS, User user) {
        Customer customer = customerService.findCustomerPublic().orElse(null);
        SystemMain systemSalesLocationId = systemMainService.findSystemPropertyName("SALES_LOCATION_ID_FOR_PLAYGROUND").orElse(null);
        Location location = new Location();
        if(systemSalesLocationId != null){
            location = locationService.findById(Long.valueOf(systemSalesLocationId.getValueprop())).orElse(null);
        }

        SalesTaking salesTaking = new SalesTaking();
        salesTaking.setNumber(playgroundRegistration.getNumber());
        salesTaking.setNumberPrefix(playgroundRegistration.getPrefix());
        salesTaking.setCounter(playgroundRegistration.getCounter());
        salesTaking.setDate(LocalDateTime.now());
        salesTaking.setCustomer( customer );
        salesTaking.setDocStatus(DocumentStatus.DRAFT.name());
        salesTaking.setType(SalesType.CASH);
        salesTaking.setLocation(location);
        salesTaking.setUserId(user.getUserId());

        //Sales taking detail
        List<SalesTakingDetail> salesTakingDetailList = new ArrayList<>();

        for(ChildrenDTO c : childrenDTOS){
            SalesTakingDetail salesTakingDetail = new SalesTakingDetail();
            salesTakingDetail.setSalesTaking(salesTaking);

            ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(c.getItemId()));
            Unit unit = itemMaster.getUomStock();

            salesTakingDetail.setItemMaster(itemMaster);
            salesTakingDetail.setSellingPrice(c.getPrice());
            salesTakingDetail.setQty(1.0);

            double total = salesTakingDetail.getSellingPrice() * salesTakingDetail.getQty();
            salesTakingDetail.setTotal(total);
            salesTakingDetail.setConvQty(1.0);
            salesTakingDetail.setUomId(unit.getUomId());
            salesTakingDetail.setUnit(unit.getUnit());

            salesTakingDetailList.add(salesTakingDetail);
        }
        salesTaking.setSalesTakingDetailList(salesTakingDetailList);
        double total = salesTaking.getSalesTakingDetailList().stream().mapToDouble(SalesTakingDetail::getTotal).sum();
        salesTaking.setAmount(total);

        return salesTakingRepository.save(salesTaking);
    }

    @Override
    public void saveAll(List<SalesTaking> salesTakings) {
        salesTakingRepository.saveAll(salesTakings);
    }

    @Override
    public ResponseEntity<?> getSalesPlaygroundByNumber(String number) {
        SalesTakingPlaygroundViewDTO content = salesTakingRepository.getSalesTakingPlaygroundByNumber(number);

        SuccessResponse<?> response = new SuccessResponse<>("success", content);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getSalesTakingsWithPlayground(String number, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SalesTakingPlaygroundViewDTO> results = salesTakingRepository.getSalesTakingsWithPlayground(number, status, pageable);

        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(results);
        SuccessPaginationResponse<?> response = new SuccessPaginationResponse<>("success", paginationResponse, results.getContent());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> save(SalesTakingDTO body) {
        SalesTaking salesTaking = new SalesTaking();
        LocalDateTime currentDate = LocalDateTime.now();

        salesTaking.setNumberPrefix("INV/TK/"+ DateUtils.formatDate(currentDate,"MM") + DateUtils.formatDate(currentDate,"YY"));
        Integer maxCounter = salesTakingRepository.findMaxCounter(salesTaking.getNumberPrefix()).orElse(0);
        maxCounter++;
        salesTaking.setCounter(maxCounter);
        String formatCounter = StringUtils.formatCounter(4,maxCounter);
        salesTaking.setNumber(salesTaking.getNumberPrefix()+"/"+formatCounter);
        salesTaking.setDate(currentDate);
        salesTaking.setUserId(body.getUserId());
        Customer customer = customerService.findCustomerPublic().orElse(null);
        if(body.getCustomerId() != null){
            customer = customerService.findById(body.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        }
        salesTaking.setCustomer(customer);
        salesTaking.setDocStatus(body.getDocStatus());

        Location location = locationService.findById(Long.valueOf(body.getLocationId())).orElseThrow(() -> new ResourceNotFoundException("Location not found"));
        salesTaking.setLocation(location);

        Set<Long> itemMasterIds = body.getSalesTakingDetails().stream().map(s -> Long.valueOf(s.getItemMasterId())).collect(Collectors.toSet());
        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemMasterIds));
        Map<Long, ItemMaster> itemMasterMap =  itemMasters.stream().collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity()));

        List<SalesTakingDetail> salesTakingDetails = new ArrayList<>();
        for(SalesTakingDetailDTO s: body.getSalesTakingDetails()){
            SalesTakingDetail salesTakingDetail = new SalesTakingDetail();
            salesTakingDetail.setSalesTaking(salesTaking);
            ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(s.getItemMasterId()));
            salesTakingDetail.setItemMaster(itemMaster);
            salesTakingDetail.setSellingPrice(s.getPrice());
            salesTakingDetail.setQty(s.getQty());
            salesTakingDetail.setDiscountItem(s.getDiscountItem());
            salesTakingDetail.setDiscountAmount(s.getDiscountItem());
            salesTakingDetail.setUomId(Long.valueOf(s.getUomId()));
            salesTakingDetail.setConvQty(s.getConvQty());

            double total = salesTakingDetail.getSellingPrice() * salesTakingDetail.getQty() - salesTakingDetail.getDiscountAmount();
            salesTakingDetail.setTotal(total);

            salesTakingDetails.add(salesTakingDetail);
        }
        salesTaking.setSalesTakingDetailList(salesTakingDetails);
        double total = salesTaking.getSalesTakingDetailList().stream().mapToDouble(SalesTakingDetail::getTotal).sum();
        salesTaking.setAmount(total);

        salesTaking = salesTakingRepository.save(salesTaking);

        if(body.getDocStatus().equalsIgnoreCase(DocumentStatus.APPROVED.name())){
            salesMapper.mappingFromSalesTaking(salesTaking, body);
        }

        SuccessResponse<?> response = new SuccessResponse<>("success", salesTaking.getNumber());
        return ResponseEntity.ok(response);

      
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> update(Long id, SalesTakingDTO body) {
        SalesTaking salesTaking = salesTakingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sales not found"));
        if(!salesTaking.getDocStatus().equalsIgnoreCase(DocumentStatus.DRAFT.name())) throw new ResourceNotFoundException("Failed updating sales");
        Customer customer = customerService.findCustomerPublic().orElse(null);
        if(body.getCustomerId() != null){
            customer = customerService.findById(body.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        }
        salesTaking.setCustomer(customer);
        salesTaking.setDocStatus(body.getDocStatus());

        Map<Long, SalesTakingDetail> salesTakingDetailMap = salesTaking.getSalesTakingDetailList().stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        Set<Long> itemMasterIds = body.getSalesTakingDetails().stream().map(s -> Long.valueOf(s.getItemMasterId())).collect(Collectors.toSet());
        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemMasterIds));
        Map<Long, ItemMaster> itemMasterMap =  itemMasters.stream().collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity()));

        List<SalesTakingDetail> salesTakingDetails = new ArrayList<>();
        for(SalesTakingDetailDTO s: body.getSalesTakingDetails()){
            SalesTakingDetail salesTakingDetail = salesTakingDetailMap.getOrDefault(Long.valueOf(s.getId()), new SalesTakingDetail());
            salesTakingDetail.setSalesTaking(salesTaking);
            ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(s.getItemMasterId()));
            salesTakingDetail.setItemMaster(itemMaster);
            salesTakingDetail.setSellingPrice(s.getPrice());
            salesTakingDetail.setQty(s.getQty());
            salesTakingDetail.setDiscountItem(s.getDiscountItem());
            salesTakingDetail.setDiscountAmount(s.getDiscountItem());
            salesTakingDetail.setUomId(Long.valueOf(s.getUomId()));
            salesTakingDetail.setConvQty(s.getConvQty());

            double total = salesTakingDetail.getSellingPrice() * salesTakingDetail.getQty() - salesTakingDetail.getDiscountAmount();
            salesTakingDetail.setTotal(total);

            salesTakingDetails.add(salesTakingDetail);
        }
        salesTaking.setSalesTakingDetailList(salesTakingDetails);
        double total = salesTaking.getSalesTakingDetailList().stream().mapToDouble(SalesTakingDetail::getTotal).sum();
        salesTaking.setAmount(total);

        salesTakingRepository.save(salesTaking);

        if(body.getDocStatus().equalsIgnoreCase(DocumentStatus.APPROVED.name())){
            salesMapper.mappingFromSalesTaking(salesTaking, body);
        }

        SuccessResponse<?> response = new SuccessResponse<>("success", salesTaking.getNumber());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> saveSalesReturn(Long id, SalesTakingDTO body) {
        //Original sales
        SalesTaking salesTaking = salesTakingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sales not found"));

        //Original sales detail
        Map<Long, SalesTakingDetail> salesTakingDetailMap = salesTaking.getSalesTakingDetailList().stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        //Qty Return before
        List<SalesTakingDetailQtyReturnDTO> salesTakingDetailQtyReturnList = salesTakingRepository.getSalesTakingDetailQtyReturnBySalesId(salesTaking.getId());

        SalesTaking salesTakingReturn = new SalesTaking();
        LocalDateTime currentDate = LocalDateTime.now();
        salesTakingReturn.setCounter(salesTaking.getCounter());
        salesTakingReturn.setNumberPrefix(salesTaking.getNumber());

        int returnCounter = salesTakingRepository.findMaxReturnCounter(salesTakingReturn.getNumberPrefix()).orElse(0);
        returnCounter++;
        salesTakingReturn.setReturnCounter(returnCounter);
        salesTakingReturn.setNumber(salesTaking.getNumber() + "/R" + (returnCounter == 1 ? "" : (returnCounter - 1)));
        salesTakingReturn.setDate(currentDate);
        salesTakingReturn.setUserId(body.getUserId());
        salesTakingReturn.setCustomer(salesTaking.getCustomer());
        salesTakingReturn.setDocStatus(DocumentStatus.DRAFT.name());
        salesTakingReturn.setLocation(salesTaking.getLocation());
        salesTakingReturn.setType(SalesType.RETUR_CASH);
        salesTakingReturn.setSalesTakingReturn(salesTaking);

        Set<Long> itemMasterIds = body.getSalesTakingDetails().stream().map(s -> Long.valueOf(s.getItemMasterId())).collect(Collectors.toSet());
        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemMasterIds));
        Map<Long, ItemMaster> itemMasterMap =  itemMasters.stream().collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity()));

        List<SalesTakingDetail> salesTakingDetails = new ArrayList<>();
        for(SalesTakingDetailDTO s: body.getSalesTakingDetails()){
            SalesTakingDetail salesTakingDetailReturn = new SalesTakingDetail();
            salesTakingDetailReturn.setSalesTaking(salesTakingReturn);
            ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(s.getItemMasterId()));
            salesTakingDetailReturn.setItemMaster(itemMaster);
            salesTakingDetailReturn.setSellingPrice(s.getPrice());

            SalesTakingDetail salesTakingDetailOriginal = salesTakingDetailMap.get(Long.parseLong(s.getId()));
            SalesTakingDetailQtyReturnDTO detailQtyReturn = salesTakingDetailQtyReturnList.stream().filter(a -> a.getId().equalsIgnoreCase(String.valueOf(Long.parseLong(s.getId())))).findFirst().orElse(null);

            if(detailQtyReturn != null && detailQtyReturn.getQtyReturn() != null){
                double qtyReturnAvailable = salesTakingDetailOriginal.getQty() - detailQtyReturn.getQtyReturn();
                if(s.getQtyReturn() >  qtyReturnAvailable){
                    throw new ResourceConflictException("Qty returned more than available qty");
                }
            }


            salesTakingDetailReturn.setQty(s.getQtyReturn());

            double discountItem = (salesTakingDetailOriginal.getDiscountItem() / salesTakingDetailOriginal.getQty()) * s.getQtyReturn();
            discountItem = Double.isNaN(discountItem) ? 0 : discountItem;
            salesTakingDetailReturn.setDiscountItem(discountItem);
            salesTakingDetailReturn.setDiscountAmount(discountItem);
            salesTakingDetailReturn.setUomId(Long.valueOf(s.getUomId()));
            salesTakingDetailReturn.setConvQty(s.getConvQty());

            salesTakingDetailReturn.setSalesTakingDetailReturn(salesTakingDetailOriginal);

            double total = salesTakingDetailReturn.getSellingPrice() * salesTakingDetailReturn.getQty() - salesTakingDetailReturn.getDiscountAmount();
            salesTakingDetailReturn.setTotal(total);

            salesTakingDetails.add(salesTakingDetailReturn);
        }
        salesTakingReturn.setSalesTakingDetailList(salesTakingDetails);
        double total = salesTakingReturn.getSalesTakingDetailList().stream().mapToDouble(SalesTakingDetail::getTotal).sum();
        salesTakingReturn.setAmount(total);

        salesTakingRepository.save(salesTakingReturn);

        SuccessResponse<?> response = new SuccessResponse<>("success", salesTakingReturn.getNumber());
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public ResponseEntity<?> updateSalesReturn(Long id, SalesTakingDTO body) {
        SalesTaking salesTakingReturn = salesTakingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sales not found"));
        //Sales Detail Return
        Map<Long, SalesTakingDetail> salesTakingDetailReturnMap = salesTakingReturn.getSalesTakingDetailList().stream()
                .collect(Collectors.toMap(BaseEntity::getId, Function.identity()));

        if(!salesTakingReturn.getDocStatus().equalsIgnoreCase(DocumentStatus.DRAFT.name())) throw new ResourceNotFoundException("Failed updating sales");
        salesTakingReturn.setUserId(body.getUserId());
        Customer customer = customerService.findCustomerPublic().orElse(null);
        if(body.getCustomerId() != null){
            customer = customerService.findById(body.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        }
        salesTakingReturn.setCustomer(customer);
        salesTakingReturn.setDocStatus(body.getDocStatus());
        salesTakingReturn.setType(SalesType.RETUR_CASH);

        //Original sales
        SalesTaking salesTaking = salesTakingReturn.getSalesTakingReturn();

        //Qty Return before
        List<SalesTakingDetailQtyReturnDTO> salesTakingDetailQtyReturnList = salesTakingRepository.getSalesTakingDetailQtyReturnBySalesId(salesTaking.getId());

        Set<Long> itemMasterIds = body.getSalesTakingDetails().stream().map(s -> Long.valueOf(s.getItemMasterId())).collect(Collectors.toSet());
        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemMasterIds));
        Map<Long, ItemMaster> itemMasterMap =  itemMasters.stream().collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity()));

        List<SalesTakingDetail> salesTakingDetails = new ArrayList<>();
        for(SalesTakingDetailDTO s: body.getSalesTakingDetails()){
            SalesTakingDetail salesTakingDetailReturn = salesTakingDetailReturnMap.getOrDefault(Long.valueOf(s.getId()), new SalesTakingDetail());
            salesTakingDetailReturn.setSalesTaking(salesTakingReturn);
            ItemMaster itemMaster = itemMasterMap.get(Long.valueOf(s.getItemMasterId()));
            salesTakingDetailReturn.setItemMaster(itemMaster);
            salesTakingDetailReturn.setSellingPrice(s.getPrice());

            SalesTakingDetail salesTakingDetailOriginal = salesTakingDetailReturn.getSalesTakingDetailReturn();
            SalesTakingDetailQtyReturnDTO detailQtyReturn = salesTakingDetailQtyReturnList.stream().filter(a -> a.getId().equalsIgnoreCase(String.valueOf(salesTakingDetailOriginal.getId()))).findFirst().orElse(null);
            if(detailQtyReturn != null && detailQtyReturn.getQtyReturn() != null){
                double qtyReturnAvailable = salesTakingDetailOriginal.getQty() - (detailQtyReturn.getQtyReturn() - salesTakingDetailReturn.getQty());
                if(s.getQty() > qtyReturnAvailable){
                    throw new ResourceConflictException("Qty returned more than available qty");
                }
            }
            salesTakingDetailReturn.setQty(s.getQty());
            salesTakingDetailReturn.setDiscountItem(s.getDiscountItem());
            salesTakingDetailReturn.setDiscountAmount(s.getDiscountItem());
            salesTakingDetailReturn.setUomId(Long.valueOf(s.getUomId()));
            salesTakingDetailReturn.setConvQty(s.getConvQty());

            double total = salesTakingDetailReturn.getSellingPrice() * salesTakingDetailReturn.getQty() - salesTakingDetailReturn.getDiscountAmount();
            salesTakingDetailReturn.setTotal(total);

            salesTakingDetails.add(salesTakingDetailReturn);
        }
        salesTakingReturn.setSalesTakingDetailList(salesTakingDetails);
        double total = salesTakingReturn.getSalesTakingDetailList().stream().mapToDouble(SalesTakingDetail::getTotal).sum();
        salesTakingReturn.setAmount(total);

        salesTakingRepository.save(salesTakingReturn);

        if(body.getDocStatus().equalsIgnoreCase(DocumentStatus.APPROVED.name())){
            salesMapper.mappingFromSalesTaking(salesTakingReturn, body);
        }

        SuccessResponse<?> response = new SuccessResponse<>("success", salesTakingReturn.getNumber());
        return ResponseEntity.ok(response);
    }


}

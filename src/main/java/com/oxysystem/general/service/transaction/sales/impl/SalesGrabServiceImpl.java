package com.oxysystem.general.service.transaction.sales.impl;

import com.oxysystem.general.dto.transaction.sales.view.SalesGrabDTO;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.db1.posmaster.ItemMaster;
import com.oxysystem.general.model.db1.transaction.sales.SalesGrab;
import com.oxysystem.general.model.db1.transaction.sales.SalesGrabDetail;
import com.oxysystem.general.repository.db1.transaction.sales.SalesGrabRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import com.oxysystem.general.service.transaction.sales.SalesGrabDetailService;
import com.oxysystem.general.service.transaction.sales.SalesGrabService;
import com.oxysystem.general.util.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SalesGrabServiceImpl implements SalesGrabService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesGrabServiceImpl.class);
    private final SalesGrabRepository salesGrabRepository;
    private final ItemMasterService itemMasterService;
    private final SalesGrabDetailService salesGrabDetailService;

    public SalesGrabServiceImpl(SalesGrabRepository salesGrabRepository, ItemMasterService itemMasterService, SalesGrabDetailService salesGrabDetailService) {
        this.salesGrabRepository = salesGrabRepository;
        this.itemMasterService = itemMasterService;
        this.salesGrabDetailService = salesGrabDetailService;
    }

    @Override
    public SalesGrab save(SalesGrab salesGrab) {
        return salesGrabRepository.save(salesGrab);
    }

    @Override
    @Transactional(value = "db1TransactionManager")
    public void deleteSalesGrab(SalesGrab salesGrab) {
        salesGrabRepository.delete(salesGrab);
    }

    @Override
    @Transactional(value = "db1TransactionManager", rollbackFor = {Throwable.class})
    public void updateSalesGrabDateByNumber(LocalDateTime date, String number) {
        salesGrabRepository.updateSalesGrabDateByNumber(date, number);
    }


    @Override
    public ResponseEntity<?> findSalesGrab(Long locationId, String date, String number, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<Object[]> objects = salesGrabRepository.findSalesGrab(locationId, date, number, pageable);
        Optional<Long> countSalesGrab = salesGrabRepository.countSalesGrab(locationId, date, number);

        List<SalesGrabDTO> salesGrabs = objects.stream()
                .map(o -> new SalesGrabDTO(
                        (String) o[0], // Number
                        (String) o[1], // Store
                        ((Timestamp) o[2]).toLocalDateTime(), // Date
                        (String) o[3] // Status
                )).collect(Collectors.toList());

        Page<SalesGrabDTO> salesGrabPage = new PageImpl<>(salesGrabs, pageable, countSalesGrab.orElse(0L));
        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(salesGrabPage);

        SuccessPaginationResponse<List<SalesGrabDTO>> response = new SuccessPaginationResponse<>("success", paginationResponse,salesGrabs);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> findSalesGrabByNumberForApi(String number) {
        SalesGrab salesGrab = salesGrabRepository.findSalesGrabByNumber(number).orElseThrow(() -> new ResourceNotFoundException("sales grab not found!"));
        Set<Long> itemMasterIds = salesGrab.getSalesGrabDetails().stream()
                .map(salesDetail -> salesDetail.getItemMaster().getItemMasterId()).collect(Collectors.toSet());

        List<ItemMaster> itemMasters = itemMasterService.findItemMastersByItemMasterIds(new ArrayList<>(itemMasterIds));
        Map<Long, ItemMaster> itemMastersMap = itemMasters.stream()
                .collect(Collectors.toMap(ItemMaster::getItemMasterId, Function.identity(),(existing, replacement) -> existing));

        SalesGrabDTO salesGrabDTO = new SalesGrabDTO();
        salesGrabDTO.setNumber(salesGrab.getNumber());
        salesGrabDTO.setDate(salesGrab.getDate());
        salesGrabDTO.setStoreId(salesGrab.getLocation().getLocationId().toString());
        salesGrabDTO.setStore(salesGrab.getLocation().getName());
        salesGrabDTO.setStatus(salesGrab.getStatus().toString());

        List<SalesGrabDTO.SalesGrabDetailDTO> salesGrabDetails = salesGrab.getSalesGrabDetails().stream()
                .map(salesGrabDetail -> {
                    ItemMaster itemMaster = itemMastersMap.get(salesGrabDetail.getItemMaster().getItemMasterId());
                    SalesGrabDTO.SalesGrabDetailDTO detail = new SalesGrabDTO.SalesGrabDetailDTO();

                    detail.setSalesGrabDetailId(String.valueOf(salesGrabDetail.getSalesGrabDetailId()));
                    detail.setIsVerified(salesGrabDetail.getIsVerified());
                    if(itemMaster != null){
                        detail.setItemId(itemMaster.getItemMasterId().toString());
                        detail.setItemName(itemMaster.getName());
                        detail.setItemCode(itemMaster.getCode());
                        detail.setItemBarcode(itemMaster.getBarcode());
                    }
                    detail.setQty(salesGrabDetail.getQty());
                    detail.setPrice(salesGrabDetail.getSellingPrice());
                    detail.setTotal(detail.getQty() * detail.getPrice());
                    detail.setVerifiedQty(salesGrabDetail.getVerifiedQty() != null ? salesGrabDetail.getVerifiedQty() : 0);

                    return detail;
                }).collect(Collectors.toList());

        salesGrabDTO.setDetails(salesGrabDetails);

        SuccessResponse<SalesGrabDTO> response = new SuccessResponse<>("success", salesGrabDTO);
        return ResponseEntity.ok(response);
    }

    @Override
    public Optional<SalesGrab> findSalesGrabByNumber(String number) {
        return salesGrabRepository.findSalesGrabByNumber(number);
    }

    @Override
    @Transactional(value = "db1TransactionManager",rollbackFor = {Throwable.class})
    public ResponseEntity<?> updateStatusSalesGrab(String number, String status) {
        SalesGrab salesGrab = salesGrabRepository.findSalesGrabByNumber(number).orElseThrow(() -> new ResourceNotFoundException("sales grab not found!"));
        salesGrab.setStatus(DocumentStatus.valueOf(status));
        if(status.equals(DocumentStatus.APPROVED.toString())){
            List<SalesGrabDetail> itemVerifieds = salesGrab.getSalesGrabDetails().stream()
                    .filter(detail -> detail.getIsVerified() == 1)
                    .collect(Collectors.toList());

            if(itemVerifieds.size() != salesGrab.getSalesGrabDetails().size()){
                throw new ResourceNotFoundException("some items have not been verified yet!");
            }
        }
        salesGrabRepository.save(salesGrab);
        SuccessResponse<String> response = new SuccessResponse<>("success", null);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional(value = "db1TransactionManager")
    public void deleteSalesGrabByNumber(String number) {
        salesGrabRepository.deleteSalesGrabByNumber(number);
    }


}

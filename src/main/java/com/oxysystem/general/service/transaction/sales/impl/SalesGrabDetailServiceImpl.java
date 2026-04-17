package com.oxysystem.general.service.transaction.sales.impl;

import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.model.tenant.transaction.sales.SalesGrabDetail;
import com.oxysystem.general.repository.tenant.transaction.sales.SalesGrabDetailRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.posmaster.ItemMasterService;
import com.oxysystem.general.service.transaction.sales.SalesGrabDetailService;
import com.oxysystem.general.service.transaction.stock.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SalesGrabDetailServiceImpl implements SalesGrabDetailService {
    private final SalesGrabDetailRepository salesGrabDetailRepository;
    private final ItemMasterService itemMasterService;
    private final LocationService locationService;
    private final StockService stockService;

    public SalesGrabDetailServiceImpl(SalesGrabDetailRepository salesGrabDetailRepository, ItemMasterService itemMasterService, LocationService locationService, StockService stockService) {
        this.salesGrabDetailRepository = salesGrabDetailRepository;
        this.itemMasterService = itemMasterService;
        this.locationService = locationService;
        this.stockService = stockService;
    }

    @Override
    public ResponseEntity<?> verifiedItem(Long salesDetailId, Long locationId, String barcode, Double qty) {
        SalesGrabDetail salesGrabDetail = salesGrabDetailRepository.findById(salesDetailId).orElseThrow(() -> new ResourceNotFoundException("sales detail not found!"));
        Location location = locationService.findById(locationId).orElseThrow(() -> new ResourceNotFoundException("location not found!"));
        ItemMaster itemMaster = itemMasterService.findItemMasterByCodeOrBarcode(barcode).orElseThrow(() -> new ResourceNotFoundException("this item not found!"));
        if(!salesGrabDetail.getItemMaster().getItemMasterId().equals(itemMaster.getItemMasterId())) throw new ResourceNotFoundException("this item is not match!");
        if(qty == null) throw new ResourceNotFoundException("quantity verify cannot be empty!");
        if(qty <= 0) throw new ResourceNotFoundException("quantity verify cannot less or equal zero!");

        double qtyVerified = Optional.ofNullable(salesGrabDetail.getVerifiedQty()).orElse(0.0) + qty;
        if(qtyVerified > salesGrabDetail.getQty()) throw new ResourceNotFoundException("quantity verity cannot more than quantity order!");

        double currentStock = stockService.getCurrentStockPerItem(itemMaster.getItemMasterId(), location.getLocationId()).orElse(0.0);
        if(qtyVerified > currentStock) throw new ResourceNotFoundException("This item is out of stock!");

        salesGrabDetail.setVerifiedQty(qtyVerified);
        salesGrabDetail.setIsVerified(qtyVerified == salesGrabDetail.getQty() ? 1 : 0);
        salesGrabDetailRepository.save(salesGrabDetail);

        SuccessResponse<String> response = new SuccessResponse<>("success",null);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public void deleteSalesGrabDetailBySalesGrabNumber(String number) {
        salesGrabDetailRepository.deleteSalesGrabDetailBySalesGrabNumber(number);
    }
}

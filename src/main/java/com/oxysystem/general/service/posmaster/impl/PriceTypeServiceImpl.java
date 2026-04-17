package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.posmaster.priceType.data.PriceTypeDTO;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.PriceType;
import com.oxysystem.general.repository.tenant.posmaster.PriceTypeRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.posmaster.PriceTypeService;
import com.oxysystem.general.util.PriceTypeUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PriceTypeServiceImpl implements PriceTypeService {
    private final PriceTypeRepository priceTypeRepository;
    private final LocationService locationService;

    public PriceTypeServiceImpl(PriceTypeRepository priceTypeRepository, LocationService locationService) {
        this.priceTypeRepository = priceTypeRepository;
        this.locationService = locationService;
    }

    @Override
    public ResponseEntity<?> getPriceByItemMaster(PriceTypeDTO body) {
        Location location = locationService.findById(body.getLocationId()).orElseThrow(() -> new ResourceNotFoundException("Location not found!"));
        PriceType priceType = priceTypeRepository.getPriceType(body.getItemMasterId()).orElse(null);
        Double price = PriceTypeUtils.getPriceByGol(priceType, location.getGolPrice());

        SuccessResponse<Double> response = new SuccessResponse<>("success", price);
        return ResponseEntity.ok(response);
    }
}

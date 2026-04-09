package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.location.view.GrabPackagingItemLocationViewDTO;
import com.oxysystem.general.dto.general.location.view.LocationGrabForSelectDTO;
import com.oxysystem.general.dto.general.location.view.LocationViewDTO;
import com.oxysystem.general.enums.grab.Product;
import com.oxysystem.general.model.db1.general.Location;
import com.oxysystem.general.repository.db1.general.LocationRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.util.ResponseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Optional<Location> findById(Long id) {
        return locationRepository.findById(id);
    }

    @Override
    public List<Location> findAllByIDs(List<Long> ids) {
        return locationRepository.findAllByIDs(ids);
    }

    @Override
    public ResponseEntity<?> getLocationsGrabForSelectForApi(String product) {
        List<Object[]> results = new ArrayList<>();
        if(product.equals(Product.GRAB_MART.name())) {
            results = locationRepository.getLocationsGrabMartForSelect();
        }else if(product.equals(Product.GRAB_FOOD.name())) {
            results = locationRepository.getLocationsGrabFoodForSelect();
        }else{
            results = locationRepository.getLocationsForSelect();
        }

        List<LocationGrabForSelectDTO> locations = results.stream()
                .map(r -> new LocationGrabForSelectDTO(
                        ((BigInteger) r[0]).toString(), // Location Id
                        (String) r[1] // Location name
                )).collect(Collectors.toList());

        SuccessResponse<List<LocationGrabForSelectDTO>> response = new SuccessResponse<>("success", locations);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getLocations(String name, String grabMerchantId, String grabFoodMerchantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LocationViewDTO> locationPage = locationRepository.getLocations(name, grabMerchantId, grabFoodMerchantId, pageable);

        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(locationPage);
        SuccessPaginationResponse<List<LocationViewDTO>> response = new SuccessPaginationResponse<>("success", paginationResponse, locationPage.getContent());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getPackagingItemLocations(String name, String product, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<GrabPackagingItemLocationViewDTO> locationPage = locationRepository.getPackagingItemLocations(name, product, pageable);

        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(locationPage);
        SuccessPaginationResponse<Object> response = new SuccessPaginationResponse<>("success", paginationResponse, locationPage.getContent());
        return ResponseEntity.ok(response);
    }

    @Override
    public List<LocationGrabForSelectDTO> getLocationsGrabForSelect(String product) {
        List<Object[]> results = new ArrayList<>();
        if(product.equals(Product.GRAB_MART.name())) {
            results = locationRepository.getLocationsGrabMartForSelect();
        }else if(product.equals(Product.GRAB_FOOD.name())) {
            results = locationRepository.getLocationsGrabFoodForSelect();
        }
        List<LocationGrabForSelectDTO> locations = results.stream()
                .map(r -> new LocationGrabForSelectDTO(
                        ((BigInteger) r[0]).toString(), // Location Id
                        (String) r[1] // Location name
                )).collect(Collectors.toList());

        return locations;
    }
}

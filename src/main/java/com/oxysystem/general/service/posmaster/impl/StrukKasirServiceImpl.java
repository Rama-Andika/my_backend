package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.dto.general.strukKasir.data.StrukKasirDTO;
import com.oxysystem.general.dto.posmaster.strukKasir.view.StrukKasirViewDTO;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.mapper.general.StrukKasirMapper;
import com.oxysystem.general.model.tenant.admin.User;
import com.oxysystem.general.model.tenant.general.Location;
import com.oxysystem.general.model.tenant.posmaster.StrukKasir;
import com.oxysystem.general.repository.tenant.posmaster.StrukKasirRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.admin.UserService;
import com.oxysystem.general.service.general.LocationService;
import com.oxysystem.general.service.posmaster.StrukKasirService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StrukKasirServiceImpl implements StrukKasirService {
    private final StrukKasirRepository strukKasirRepository;
    private final LocationService locationService;
    private final UserService userService;

    // Mapper
    private final StrukKasirMapper strukKasirMapper;

    public StrukKasirServiceImpl(StrukKasirRepository strukKasirRepository, LocationService locationService, UserService userService, StrukKasirMapper strukKasirMapper) {
        this.strukKasirRepository = strukKasirRepository;
        this.locationService = locationService;
        this.userService = userService;
        this.strukKasirMapper = strukKasirMapper;
    }

    @Override
    public Optional<StrukKasir> findByGrabMerchantId(String merchantId) {
        return strukKasirRepository.findByGrabMerchantId(merchantId);
    }

    @Override
    public Optional<StrukKasir> findByGrabFoodMerchantId(String merchantId) {
        return strukKasirRepository.findByGrabFoodMerchantId(merchantId);
    }

    @Override
    public List<StrukKasir> findStrukKasirGrabMerchantIDNotNull() {
        return strukKasirRepository.findStrukKasirGrabMerchantIDNotNull();
    }

    @Override
    public List<StrukKasir> findStrukKasirGrabFoodMerchantIDNotNull() {
        return strukKasirRepository.findStrukKasirGrabFoodMerchantIDNotNull();
    }

    @Override
    public Optional<StrukKasir> findStrukKasirByLocationId(Long locationId) {
        return strukKasirRepository.findStrukKasirByLocationId(locationId);
    }

    @Override
    public List<StrukKasir> findStrukKasirByLocationIds(List<Long> locationIds) {
        return strukKasirRepository.findStrukKasirByLocationIds(locationIds);
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public ResponseEntity<?> save(StrukKasirDTO body) {
        Location location = locationService.findById(body.getLocationId()).orElseThrow(() -> new ResourceNotFoundException("location not found!"));
        User user = userService.findById(body.getUserId()).orElseThrow(() -> new ResourceNotFoundException("user not found!"));
        StrukKasir strukKasir = strukKasirRepository.findStrukKasirByLocationId(location.getLocationId()).orElse(null);
        if (strukKasir != null && strukKasir.getGrabMerchantId() != null && !strukKasir.getGrabMerchantId().isEmpty() && (!strukKasir.getGrabMerchantId().equalsIgnoreCase(body.getGrabMerchantId()))) {
            throw new ResourceNotFoundException("cannot update grab merchant id");
        }

        if (strukKasir != null && strukKasir.getGrabFoodMerchantId() != null && !strukKasir.getGrabFoodMerchantId().isEmpty() && (!strukKasir.getGrabFoodMerchantId().equalsIgnoreCase(body.getGrabFoodMerchantId()))) {
            throw new ResourceNotFoundException("cannot update grab food merchant id");
        }

        body.setUserId(user.getUserId());
        body.setLocationId(location.getLocationId());
        strukKasir = strukKasirMapper.toEntity(strukKasir, body);
        strukKasir = strukKasirRepository.save(strukKasir);

        SuccessResponse<String> response = new SuccessResponse<>("success", String.valueOf(strukKasir.getStrukKasirId()));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getStrukKasirByLocationId(Long locationId) {
        StrukKasirViewDTO strukKasir = strukKasirRepository.findByLocationId(locationId).orElseThrow(() -> new ResourceNotFoundException("struk kasir not found!"));
        SuccessResponse<?> response = new SuccessResponse<>("success", strukKasir);
        return ResponseEntity.ok(response);
    }
}

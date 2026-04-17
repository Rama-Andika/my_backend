package com.oxysystem.general.service.transaction.impl;

import com.oxysystem.general.dto.transaction.salesTakingDetail.view.SalesTakingDetailViewDTO;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.mapper.transaction.SalesTakingDetailMapper;
import com.oxysystem.general.model.tenant.transaction.SalesTakingDetail;
import com.oxysystem.general.repository.tenant.transaction.SalesTakingDetailRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.transaction.SalesTakingDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class SalesTakingDetailServiceImpl implements SalesTakingDetailService {
    private final SalesTakingDetailRepository salesTakingDetailRepository;

    public SalesTakingDetailServiceImpl(SalesTakingDetailRepository salesTakingDetailRepository) {
        this.salesTakingDetailRepository = salesTakingDetailRepository;
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<?> deleteSalesTakingDetailById(Long salesTakingDetailId) {
        SalesTakingDetail salesTakingDetail = salesTakingDetailRepository.findById(salesTakingDetailId).orElseThrow(() -> new ResourceNotFoundException("Detail not found"));
        salesTakingDetailRepository.delete(salesTakingDetail);
        SuccessResponse<?> response = new SuccessResponse<>("success", null);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public ResponseEntity<?> deleteSalesTakingDetailByIds(Set<Long> salesTakingDetailIds) {
        List<SalesTakingDetail> salesTakingDetails = salesTakingDetailRepository.findAllById(salesTakingDetailIds);
        salesTakingDetailRepository.deleteAll(salesTakingDetails);

        SuccessResponse<?> response = new SuccessResponse<>("success", null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> findById(Long salesTakingDetailId) {
        SalesTakingDetail salesTakingDetail = salesTakingDetailRepository.findById(salesTakingDetailId).orElseThrow(() -> new ResourceNotFoundException("Detail not found"));
        SalesTakingDetailViewDTO data = SalesTakingDetailMapper.mappingSalesTakingDetailViewDto(salesTakingDetail);

        SuccessResponse<?> response = new SuccessResponse<>("success", data);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> findByIds(Set<Long> salesTakingDetailIds) {
        List<SalesTakingDetail> salesTakingDetails = salesTakingDetailRepository.findAllById(salesTakingDetailIds);
        List<SalesTakingDetailViewDTO> contents = SalesTakingDetailMapper.mappingSalesTakingDetailViewDtos(salesTakingDetails);

        SuccessResponse<?> response = new SuccessResponse<>("success", contents);
        return ResponseEntity.ok(response);
    }
}

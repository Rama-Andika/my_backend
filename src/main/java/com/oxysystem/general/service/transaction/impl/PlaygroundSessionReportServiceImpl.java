package com.oxysystem.general.service.transaction.impl;

import com.oxysystem.general.dto.transaction.playgroundSession.report.PlaygroundSessionReportDTO;
import com.oxysystem.general.repository.db1.transaction.PlaygroundSessionReportRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.service.transaction.PlaygroundSessionReportService;
import com.oxysystem.general.util.ResponseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PlaygroundSessionReportServiceImpl implements PlaygroundSessionReportService {
    private final PlaygroundSessionReportRepository playgroundSessionReportRepository;

    public PlaygroundSessionReportServiceImpl(PlaygroundSessionReportRepository playgroundSessionReportRepository) {
        this.playgroundSessionReportRepository = playgroundSessionReportRepository;
    }

    @Override
    public ResponseEntity<?> getPlaygroundSessionReport(String registrationNumber, int page, int size) {
        Pageable  pageable = PageRequest.of(page, size);
        Page<PlaygroundSessionReportDTO> result = playgroundSessionReportRepository.getPlaygroundSessionReport(registrationNumber, pageable);
        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(result);
        SuccessPaginationResponse<?> response = new SuccessPaginationResponse<>("success", paginationResponse, result.getContent());
        return ResponseEntity.ok(response);
    }
}

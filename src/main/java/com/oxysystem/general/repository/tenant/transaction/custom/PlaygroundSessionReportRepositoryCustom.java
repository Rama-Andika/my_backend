package com.oxysystem.general.repository.tenant.transaction.custom;

import com.oxysystem.general.dto.transaction.playgroundSession.report.PlaygroundSessionReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaygroundSessionReportRepositoryCustom {
    Page<PlaygroundSessionReportDTO> getPlaygroundSessionReport(String registrationNumber, Pageable pageable);
}

package com.oxysystem.general.repository.tenant.transaction.custom;

import com.oxysystem.general.dto.transaction.playgroundSession.view.PlaygroundSessionViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaygroundSessionRepositoryCustom {
    List<PlaygroundSessionViewDTO> getLiveSession();
    Page<PlaygroundSessionViewDTO> getEndSession(String date, Pageable pageable);
}

package com.oxysystem.general.service.transaction.impl;

import com.oxysystem.general.dto.transaction.playgroundSession.view.PlaygroundSessionViewDTO;
import com.oxysystem.general.exception.ResourceNotFoundException;
import com.oxysystem.general.model.tenant.transaction.PlaygroundSession;
import com.oxysystem.general.repository.tenant.transaction.PlaygroundSessionRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.transaction.PlaygroundSessionService;
import com.oxysystem.general.util.ResponseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlaygroundSessionServiceImpl implements PlaygroundSessionService {
    private final PlaygroundSessionRepository playgroundSessionRepository;

    public PlaygroundSessionServiceImpl(PlaygroundSessionRepository playgroundSessionRepository) {
        this.playgroundSessionRepository = playgroundSessionRepository;
    }

    @Override
    public PlaygroundSession save(PlaygroundSession playgroundSession) {
        return playgroundSessionRepository.save(playgroundSession);
    }

    @Override
    public void stopSession(Long id) {
        PlaygroundSession session = playgroundSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playground Session not found"));

        session.setIsActive(0);
        playgroundSessionRepository.save(session);
    }

    @Override
    public void autoDeactivateExpiredSessions() {
        List<Long> expiredIds = playgroundSessionRepository.findExpiredActiveSessionIds();

        if (!expiredIds.isEmpty()) {
            playgroundSessionRepository.deactivateExpiredSessions(expiredIds);
        }
    }

    @Override
    public ResponseEntity<?> getLiveSession() {
        List<PlaygroundSessionViewDTO> results = playgroundSessionRepository.getLiveSession();

        SuccessResponse<?> response = new SuccessResponse<>("success", results);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> getEndSession(String date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PlaygroundSessionViewDTO> results = playgroundSessionRepository.getEndSession(date, pageable);

        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(results);
        SuccessPaginationResponse<?> response = new SuccessPaginationResponse<>("success", paginationResponse, results.getContent());
        return ResponseEntity.ok(response);
    }
}

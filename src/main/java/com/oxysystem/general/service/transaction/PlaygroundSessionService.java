package com.oxysystem.general.service.transaction;

import com.oxysystem.general.model.db1.transaction.PlaygroundSession;
import org.springframework.http.ResponseEntity;

public interface PlaygroundSessionService {
    PlaygroundSession save(PlaygroundSession playgroundSession);

    void stopSession(Long id);

    void autoDeactivateExpiredSessions();

    ResponseEntity<?> getLiveSession();

    ResponseEntity<?> getEndSession(String date, int page, int size);
}

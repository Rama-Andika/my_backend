package com.oxysystem.general.service.general;

import com.oxysystem.general.model.db1.general.PlaygroundRegistration;
import org.springframework.http.ResponseEntity;

public interface PlaygroundRegistrationService {
    PlaygroundRegistration save(PlaygroundRegistration playgroundRegistration);

    ResponseEntity<?> getPlaygroundRegistrations(String number, int page, int size);
}

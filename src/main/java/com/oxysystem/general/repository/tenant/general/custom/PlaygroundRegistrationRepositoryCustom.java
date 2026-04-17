package com.oxysystem.general.repository.tenant.general.custom;

import com.oxysystem.general.dto.general.playgroundRegistration.view.PlaygroundRegistrationViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaygroundRegistrationRepositoryCustom {
    Page<PlaygroundRegistrationViewDTO> getPlaygroundRegistrations(String number, Pageable pageable);
}

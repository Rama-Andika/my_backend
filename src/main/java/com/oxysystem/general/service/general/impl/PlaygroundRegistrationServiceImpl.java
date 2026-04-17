package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.playgroundRegistration.view.PlaygroundRegistrationViewDTO;
import com.oxysystem.general.model.tenant.general.PlaygroundRegistration;
import com.oxysystem.general.repository.tenant.general.PlaygroundRegistrationRepository;
import com.oxysystem.general.response.PaginationResponse;
import com.oxysystem.general.response.SuccessPaginationResponse;
import com.oxysystem.general.service.general.PlaygroundRegistrationService;
import com.oxysystem.general.util.DateUtils;
import com.oxysystem.general.util.ResponseUtils;
import com.oxysystem.general.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlaygroundRegistrationServiceImpl implements PlaygroundRegistrationService {
    private final PlaygroundRegistrationRepository playgroundRegistrationRepository;

    public PlaygroundRegistrationServiceImpl(PlaygroundRegistrationRepository playgroundRegistrationRepository) {
        this.playgroundRegistrationRepository = playgroundRegistrationRepository;
    }

    @Override
    public PlaygroundRegistration save(PlaygroundRegistration playgroundRegistration) {
        LocalDateTime currentDate = LocalDateTime.now();
        playgroundRegistration.setPrefix("REG/PG/"+ DateUtils.formatDate(currentDate,"MM") + DateUtils.formatDate(currentDate,"YY"));
        Integer maxCounter = playgroundRegistrationRepository.findMaxCounter(playgroundRegistration.getPrefix()).orElse(0);
        maxCounter++;

        playgroundRegistration.setCounter(maxCounter);
        String formatCounter = StringUtils.formatCounter(4,maxCounter);
        playgroundRegistration.setNumber(playgroundRegistration.getPrefix()+"/"+formatCounter);
        return playgroundRegistrationRepository.save(playgroundRegistration);
    }

    @Override
    public ResponseEntity<?> getPlaygroundRegistrations(String number, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PlaygroundRegistrationViewDTO> results = playgroundRegistrationRepository.getPlaygroundRegistrations(number, pageable);

        PaginationResponse paginationResponse = ResponseUtils.createPaginationResponse(results);
        SuccessPaginationResponse<?> response = new SuccessPaginationResponse<>("success", paginationResponse, results.getContent());
        return ResponseEntity.ok(response);
    }
}

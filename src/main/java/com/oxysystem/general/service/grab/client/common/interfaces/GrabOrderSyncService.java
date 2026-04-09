package com.oxysystem.general.service.grab.client.common.interfaces;

import com.oxysystem.general.dto.grab.data.ListOrderResponseDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GrabOrderSyncService {
    ResponseEntity<?> listOrder(String token, String merchantID, String date, Integer page, List<String> orderIDs);
    ListOrderResponseDTO getListOrder(String token, String merchantID, String date, Integer page, List<String> orderIDs);
    ResponseEntity<?> listOrderMappingToListOrderDTO(Long locationId, String date, Integer page, List<String> orderIDs);
    Mono<ListOrderResponseDTO> getListOrderReactive(String token, String merchantID, String date, Integer page, List<String> orderIDs);
}

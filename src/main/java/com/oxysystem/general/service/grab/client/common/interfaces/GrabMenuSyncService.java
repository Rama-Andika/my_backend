package com.oxysystem.general.service.grab.client.common.interfaces;

import com.oxysystem.general.dto.grab.data.BatchUpdateMenuRequestDTO;
import com.oxysystem.general.dto.grab.data.SubmitOrderRequestDTO;
import com.oxysystem.general.dto.grab.data.UpdateMenuNotificationRequestDTO;
import com.oxysystem.general.dto.grab.data.UpdateMenuRecordRequestDTO;
import com.oxysystem.general.model.db1.general.Location;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GrabMenuSyncService {
    ResponseEntity<?> UpdateMenuNotification(String token, UpdateMenuNotificationRequestDTO request);

    Mono<Void> updateMenuNotifyReactive(String token, UpdateMenuNotificationRequestDTO request);

    default ResponseEntity<?> listMartCategories(String token, String countryCode){
        return ResponseEntity.ok().build();
    }

    ResponseEntity<?> updateMenuRecord(String token, UpdateMenuRecordRequestDTO updateMenuRecordRequestDTO);

    ResponseEntity<?> batchUpdateMenu(String token, BatchUpdateMenuRequestDTO batchUpdateMenuRequestDTO);

    void batchUpdateMenu(BatchUpdateMenuRequestDTO batchUpdateMenuRequestDTO);

    Mono<Void> batchUpdateMenuReactive(BatchUpdateMenuRequestDTO dto, String token);

    BatchUpdateMenuRequestDTO createBatchUpdate(List<SubmitOrderRequestDTO.Item> items, String merchantId);

    BatchUpdateMenuRequestDTO createBatchUpdate(List<SubmitOrderRequestDTO.Item> items, String merchantId, Location location);
}

package com.oxysystem.general.service.transaction.sales;

import com.oxysystem.general.model.tenant.transaction.sales.SalesGrab;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;


public interface SalesGrabService {
    SalesGrab save(SalesGrab salesGrab);
    void deleteSalesGrab(SalesGrab salesGrab);
    void updateSalesGrabDateByNumber(LocalDateTime date, String number);
    ResponseEntity<?> findSalesGrab(Long locationId, String date, String number, int page, int size);
    ResponseEntity<?> findSalesGrabByNumberForApi(String number);
    Optional<SalesGrab> findSalesGrabByNumber(String number);
    ResponseEntity<?> updateStatusSalesGrab(String number, String status);
    void deleteSalesGrabByNumber(String number);
}

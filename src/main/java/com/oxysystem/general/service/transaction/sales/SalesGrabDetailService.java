package com.oxysystem.general.service.transaction.sales;

import org.springframework.http.ResponseEntity;

public interface SalesGrabDetailService {
    ResponseEntity<?> verifiedItem(Long salesDetailId, Long locationId, String barcode, Double qty);

    void deleteSalesGrabDetailBySalesGrabNumber(String number);
}

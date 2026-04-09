package com.oxysystem.general.controller.transaction;

import com.oxysystem.general.service.transaction.sales.SalesGrabDetailService;
import com.oxysystem.general.service.transaction.sales.SalesGrabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SalesGrabController {
    private final SalesGrabService salesGrabService;
    private final SalesGrabDetailService salesGrabDetailService;

    public SalesGrabController(SalesGrabService salesGrabService, SalesGrabDetailService salesGrabDetailService) {
        this.salesGrabService = salesGrabService;
        this.salesGrabDetailService = salesGrabDetailService;
    }

    @GetMapping("/sales-grab")
    public ResponseEntity<?> findSalesGrab(Long locationId, String date, String number,
                                           @RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "20") int size){
        return salesGrabService.findSalesGrab(locationId, date, number, page, size);
    }

    @GetMapping("/sales-grab/{number}")
    public ResponseEntity<?> findSalesGrabByNumber(@PathVariable String number){
        return salesGrabService.findSalesGrabByNumberForApi(number);
    }

    @GetMapping("/sales-grab/verified-item")
    public ResponseEntity<?> verifiedItem(@RequestParam Long salesDetailId,
                                          @RequestParam Long locationId,
                                          @RequestParam String barcode,
                                          @RequestParam Double qty){
        return salesGrabDetailService.verifiedItem(salesDetailId, locationId,barcode,qty);
    }

    @PatchMapping("/sales-grab/update-status/{number}")
    public ResponseEntity<?> updateStatusSalesGrab(@PathVariable String number, @RequestParam String status){
        return salesGrabService.updateStatusSalesGrab(number, status);
    }
}

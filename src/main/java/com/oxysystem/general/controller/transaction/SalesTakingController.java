package com.oxysystem.general.controller.transaction;

import com.oxysystem.general.dto.transaction.salesTaking.data.SalesTakingDTO;
import com.oxysystem.general.service.transaction.SalesTakingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Validated
public class SalesTakingController {
    private final SalesTakingService salesTakingService;

    public SalesTakingController(SalesTakingService salesTakingService) {
        this.salesTakingService = salesTakingService;
    }

    @GetMapping("/playground/sales")
    public ResponseEntity<?> getSalesPlaygroundByNumber(@RequestParam String number) {
        return salesTakingService.getSalesPlaygroundByNumber(number);
    }

    @GetMapping("/sales-takings/playground")
    public ResponseEntity<?> getSalesTakingsWithPlayground(String number, String status,
                                                           @RequestParam(required = false, defaultValue = "0")int page,
                                                           @RequestParam(required = false, defaultValue = "25")int size) {
        return salesTakingService.getSalesTakingsWithPlayground(number, status, page, size);
    }

    @PostMapping("/sales-taking")
    public ResponseEntity<?> save(@Valid @RequestBody SalesTakingDTO salesTaking) {
        return salesTakingService.save(salesTaking);
    }

    @PutMapping("/sales-taking/{id}/edit")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody SalesTakingDTO salesTaking) {
        return salesTakingService.update(id, salesTaking);
    }

    @PostMapping("/sales-taking/return/{id}")
    public ResponseEntity<?> saveSalesReturn(@PathVariable Long id, @Valid @RequestBody SalesTakingDTO salesTaking) {
        return salesTakingService.saveSalesReturn(id, salesTaking);
    }

    @PutMapping("/sales-taking/return/edit/{id}")
    public ResponseEntity<?> updateSalesReturn(@PathVariable Long id, @Valid @RequestBody SalesTakingDTO salesTaking) {
        return salesTakingService.updateSalesReturn(id, salesTaking);
    }
}

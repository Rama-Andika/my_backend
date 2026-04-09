package com.oxysystem.general.controller.transaction;

import com.oxysystem.general.service.transaction.SalesTakingDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SalesTakingDetailController {
    private final SalesTakingDetailService salesTakingDetailService;

    public SalesTakingDetailController(SalesTakingDetailService salesTakingDetailService) {
        this.salesTakingDetailService = salesTakingDetailService;
    }

    @DeleteMapping("/sales-taking-detail/{id}")
    public ResponseEntity<?> deleteSalesTakingDetailById(@PathVariable Long id) {
        return salesTakingDetailService.deleteSalesTakingDetailById(id);
    }

    @DeleteMapping("/sales-taking-details")
    public ResponseEntity<?> deleteSalesTakingDetails(@RequestParam Set<Long> salesTakingDetailIds) {
        return salesTakingDetailService.deleteSalesTakingDetailByIds(salesTakingDetailIds);
    }
    @GetMapping("/sales-taking-detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
       return salesTakingDetailService.findById(id);
    }

    @GetMapping("/sales-taking-details")
    public ResponseEntity<?> findById(@RequestParam Set<Long> salesTakingDetailIds) {
        return salesTakingDetailService.findByIds(salesTakingDetailIds);
    }
}

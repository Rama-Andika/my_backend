package com.oxysystem.general.controller.general;

import com.oxysystem.general.service.general.DashboardGrabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class DashboardController {
    private final DashboardGrabService dashboardGrabService ;

    public DashboardController(DashboardGrabService dashboardGrabService) {
        this.dashboardGrabService = dashboardGrabService;
    }

    @GetMapping("/dashboard/summary")
    public ResponseEntity<?> getDashboardSummaryGrab(@RequestParam String product) {
        return dashboardGrabService.getDashboardSummaryGrab(product);
    }
    @GetMapping("/dashboard/recent-sales")
    public ResponseEntity<?> getRecentSalesByCashMasterType(int type){
        return dashboardGrabService.getRecentSalesByCashMasterType(type);
    }
}

package com.oxysystem.general.controller.general;

import com.oxysystem.general.dto.general.ApiAppSyncSetup.data.ApiAppSyncSetupDTO;
import com.oxysystem.general.service.general.ApiAppSyncSetupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
public class ApiAppSyncSetupController {
    private final ApiAppSyncSetupService apiAppSyncSetupService;

    public ApiAppSyncSetupController(ApiAppSyncSetupService apiAppSyncSetupService) {
        this.apiAppSyncSetupService = apiAppSyncSetupService;
    }

    @PostMapping("/sync/setup")
    public ResponseEntity<?> save(@RequestBody ApiAppSyncSetupDTO body){
        return apiAppSyncSetupService.save(body);
    }
}

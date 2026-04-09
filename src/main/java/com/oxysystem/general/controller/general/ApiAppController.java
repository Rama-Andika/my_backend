package com.oxysystem.general.controller.general;

import com.oxysystem.general.dto.general.apiApp.data.ApiAppDTO;
import com.oxysystem.general.service.general.ApiAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app")
public class ApiAppController {
    private final ApiAppService apiAppService;

    public ApiAppController(ApiAppService apiAppService) {
        this.apiAppService = apiAppService;
    }

    @PostMapping("")
    public ResponseEntity<?> save(@RequestBody ApiAppDTO body){
        return apiAppService.save(body);
    }
}

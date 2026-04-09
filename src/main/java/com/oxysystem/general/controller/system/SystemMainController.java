package com.oxysystem.general.controller.system;

import com.oxysystem.general.service.system.SystemMainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class SystemMainController {
    private final SystemMainService systemMainService;

    public SystemMainController(SystemMainService systemMainService) {
        this.systemMainService = systemMainService;
    }

    @GetMapping("/system-main")
    public ResponseEntity<?> findSystemMain(String name) {
        return systemMainService.findSystemPropertyNameForApi(name);
    }
}

package com.oxysystem.general.controller.general;

import com.oxysystem.general.dto.general.strukKasir.data.StrukKasirDTO;
import com.oxysystem.general.service.posmaster.StrukKasirService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class StrukKasirController {
    private final StrukKasirService strukKasirService;

    public StrukKasirController(StrukKasirService strukKasirService) {
        this.strukKasirService = strukKasirService;
    }

    @PostMapping("/struk-kasir")
    public ResponseEntity<?> save(@RequestBody StrukKasirDTO body){
        return strukKasirService.save(body);
    }

    @GetMapping("/struk-kasir/{locationId}")
    public ResponseEntity<?> getStrukKasirByLocationId(@PathVariable Long locationId) {
        return strukKasirService.getStrukKasirByLocationId(locationId);
    }
}

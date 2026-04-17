package com.oxysystem.general.controller.master;

import com.oxysystem.general.service.TenantProvisioningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private TenantProvisioningService tenantProvisioningService;

    // API untuk mendaftarkan Klien (Toko) baru
    @PostMapping("/register-client")
    public ResponseEntity<?> registerNewClient(
            @RequestParam String clientName,
            @RequestParam String dbUsername,
            @RequestParam String dbPassword) {

        try {
            // Memanggil "Sihir" pembuat database dan eksekutor Flyway
            String result = tenantProvisioningService.createNewTenant(clientName, dbUsername, dbPassword);
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Gagal membuat klien: " + e.getMessage());
        }
    }
}
package com.oxysystem.general.controller.general;

import com.oxysystem.general.service.general.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers(String name,
                                          @RequestParam(required = false, defaultValue = "0")int page,
                                          @RequestParam(required = false, defaultValue = "25")int size) {
        return customerService.getCustomers(name, page, size);
    }
}

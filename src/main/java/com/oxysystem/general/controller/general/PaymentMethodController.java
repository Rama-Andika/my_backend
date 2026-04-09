package com.oxysystem.general.controller.general;

import com.oxysystem.general.service.general.PaymentMethodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping("/payment-method/for-select")
    public ResponseEntity<?> getPaymentMethods(){
        return paymentMethodService.getPaymentMethods();
    }
}

package com.oxysystem.general.service.general;

import com.oxysystem.general.model.tenant.general.PaymentMethod;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface PaymentMethodService {
    Optional<PaymentMethod> findById(Long id);
    ResponseEntity<?> getPaymentMethods();
}

package com.oxysystem.general.service.general.impl;

import com.oxysystem.general.dto.general.paymentMethod.view.PaymentMethodViewDTO;
import com.oxysystem.general.model.db1.general.PaymentMethod;
import com.oxysystem.general.repository.db1.general.PaymentMethodRepository;
import com.oxysystem.general.response.SuccessResponse;
import com.oxysystem.general.service.general.PaymentMethodService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public Optional<PaymentMethod> findById(Long id) {
        return paymentMethodRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> getPaymentMethods() {
        List<PaymentMethodViewDTO> paymentMethods = paymentMethodRepository.getPaymentMethods();
        SuccessResponse<Object> response = new SuccessResponse<>("success", paymentMethods);
        return ResponseEntity.ok(response);
    }
}

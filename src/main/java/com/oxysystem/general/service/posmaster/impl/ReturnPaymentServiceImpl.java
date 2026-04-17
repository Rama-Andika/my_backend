package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.tenant.posmaster.ReturnPayment;
import com.oxysystem.general.repository.tenant.posmaster.ReturnPaymentRepository;
import com.oxysystem.general.service.posmaster.ReturnPaymentService;
import org.springframework.stereotype.Service;

@Service
public class ReturnPaymentServiceImpl implements ReturnPaymentService {
    private final ReturnPaymentRepository paymentRepository;

    public ReturnPaymentServiceImpl(ReturnPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public ReturnPayment save(ReturnPayment returnPayment) {
        return paymentRepository.save(returnPayment);
    }
}

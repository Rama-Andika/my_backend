package com.oxysystem.general.service.posmaster.impl;

import com.oxysystem.general.model.tenant.posmaster.Payment;
import com.oxysystem.general.repository.tenant.posmaster.PaymentRepository;
import com.oxysystem.general.service.posmaster.PaymentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void saveAll(List<Payment> payments) {
        paymentRepository.saveAll(payments);
    }

    @Override
    public Optional<Integer> countPaymentByCashCashierId(Long id) {
        return paymentRepository.countPaymentByCashCashierId(id);
    }

    @Override
    public List<Payment> getPaymentsBySalesIds(List<Long> salesIds) {
        return paymentRepository.findPaymentsBySalesId(salesIds);
    }
}

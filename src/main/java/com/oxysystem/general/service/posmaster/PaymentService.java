package com.oxysystem.general.service.posmaster;

import com.oxysystem.general.model.tenant.posmaster.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment save(Payment payment);
    void saveAll(List<Payment> payments);
    Optional<Integer> countPaymentByCashCashierId(Long id);
    List<Payment> getPaymentsBySalesIds(List<Long> salesIds);
}

package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT COUNT(p) FROM Payment p " +
            "JOIN p.sales s " +
            "WHERE s.cashCashier.cashCashierId = :cashCashierId")
    Optional<Integer> countPaymentByCashCashierId(@Param("cashCashierId") Long cashCashierId);

    @Query("SELECT p FROM Payment p WHERE p.sales.salesId IN :salesIds")
    List<Payment> findPaymentsBySalesId(@Param("salesIds") List<Long> salesIds);
}

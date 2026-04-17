package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.ReturnPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnPaymentRepository extends JpaRepository<ReturnPayment, Long> {
}

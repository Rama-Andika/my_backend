package com.oxysystem.general.repository.db1.general;

import com.oxysystem.general.dto.general.paymentMethod.view.PaymentMethodViewDTO;
import com.oxysystem.general.model.db1.general.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    @Query("SELECT new com.oxysystem.general.dto.general.paymentMethod.view.PaymentMethodViewDTO(CAST(p.paymentMethodId as string), p.description, p.sortOrder, p.posCode, p.type) " +
            "FROM PaymentMethod p ORDER BY p.sortOrder asc ")
    List<PaymentMethodViewDTO> getPaymentMethods();
}

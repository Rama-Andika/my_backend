package com.oxysystem.general.repository.tenant.transaction.sales;

import com.oxysystem.general.model.tenant.transaction.sales.SalesGrabDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SalesGrabDetailRepository extends JpaRepository<SalesGrabDetail, Long> {
    @Modifying
    @Query(value =  "DELETE sd FROM pos_sales_grab_detail sd INNER JOIN pos_sales_grab s " +
                    "ON sd.sales_grab_id = s.sales_grab_id WHERE s.number = :number", nativeQuery = true)
    void deleteSalesGrabDetailBySalesGrabNumber(@Param("number") String number);
}

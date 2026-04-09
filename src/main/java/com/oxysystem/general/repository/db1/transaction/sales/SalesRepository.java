package com.oxysystem.general.repository.db1.transaction.sales;

import com.oxysystem.general.model.db1.transaction.sales.Sales;
import com.oxysystem.general.repository.db1.transaction.sales.custom.SalesRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long>, SalesRepositoryCustom {
    @Query("SELECT COUNT(s) FROM Sales s WHERE s.cashCashier.cashCashierId = :cashCashierId")
    Optional<Integer> countSalesByCashCashierId(@Param("cashCashierId") Long cashCashierId);

    @Query("SELECT COUNT(s) FROM Sales s " +
            "JOIN s.salesDetails sd " +
            "WHERE s.cashCashier.cashCashierId = :cashCashierId")
    Optional<Integer> countSalesDetailByCashCashierId(@Param("cashCashierId") Long cashCashierId);

    @Query("SELECT s FROM Sales s WHERE s.number = :number")
    Optional<Sales> findSalesByNumber(@Param("number") String number);

    @Query("SELECT s FROM Sales s WHERE s.number IN :numbers")
    List<Sales> findAllSalesByNumber(@Param("numbers") List<String> numbers);
}

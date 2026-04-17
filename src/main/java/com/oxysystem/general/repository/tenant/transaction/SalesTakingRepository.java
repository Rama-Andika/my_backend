package com.oxysystem.general.repository.tenant.transaction;

import com.oxysystem.general.model.tenant.transaction.SalesTaking;
import com.oxysystem.general.repository.tenant.transaction.custom.SalesTakingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SalesTakingRepository extends JpaRepository<SalesTaking, Long>, SalesTakingRepositoryCustom {
    @Query("SELECT MAX(s.counter) FROM SalesTaking s WHERE s.numberPrefix = :prefixNumber")
    Optional<Integer> findMaxCounter(@Param("prefixNumber")String prefixNumber);

    @Query("SELECT MAX(s.returnCounter) FROM SalesTaking s WHERE s.numberPrefix = :prefixNumber")
    Optional<Integer> findMaxReturnCounter(@Param("prefixNumber")String prefixNumber);

    @Query("SELECT s FROM SalesTaking s WHERE s.number = :number")
    Optional<SalesTaking> findByNumber(@Param("number")String number);
}

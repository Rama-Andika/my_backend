package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.CashCashier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CashCashierRepository extends JpaRepository<CashCashier, Long> {
    @Query(value = "SELECT c.* FROM pos_cash_cashier c " +
            "INNER JOIN pos_cash_master cm " +
            "ON c.cash_master_id = cm.cash_master_id " +
            "INNER JOIN pos_location l " +
            "ON cm.location_id = l.location_id " +
            "WHERE c.user_id = :userId " +
            "AND DATE(c.date_open) = :date " +
            "AND l.location_id = :locationId " +
            "AND c.cash_master_id = :cashMasterId LIMIT 1", nativeQuery = true)
    Optional<CashCashier> findCashCashier(@Param("userId") String userId,
                                          @Param("date") LocalDate date,
                                          @Param("locationId") Long locationId,
                                          @Param("cashMasterId") Long cashMasterId);
}

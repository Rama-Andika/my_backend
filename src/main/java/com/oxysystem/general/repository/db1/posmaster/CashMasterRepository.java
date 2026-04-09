package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.CashMaster;
import com.oxysystem.general.repository.db1.posmaster.custom.CashMasterRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CashMasterRepository extends JpaRepository<CashMaster, Long>, CashMasterRepositoryCustom {
    @Query("SELECT MAX(c.cashierNumber) FROM CashMaster c")
    Optional<Integer> maxCashierNumber();
}

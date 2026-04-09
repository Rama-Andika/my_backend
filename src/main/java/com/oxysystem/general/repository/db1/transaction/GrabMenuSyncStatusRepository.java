package com.oxysystem.general.repository.db1.transaction;

import com.oxysystem.general.model.db1.transaction.GrabMenuSyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrabMenuSyncStatusRepository extends JpaRepository<GrabMenuSyncStatus, Long> {
    @Query("SELECT g FROM GrabMenuSyncStatus g WHERE g.merchantId = :merchantId")
    Optional<GrabMenuSyncStatus> findByMerchantId(@Param("merchantId") String merchantId);
}

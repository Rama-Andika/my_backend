package com.oxysystem.general.repository.tenant.general;

import com.oxysystem.general.model.tenant.general.ApiAppSyncSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiAppSyncSetupRepository extends JpaRepository<ApiAppSyncSetup, Long> {
    @Query("SELECT a FROM ApiAppSyncSetup a WHERE a.status = 1 AND a.apiApp.apiAppId = :apiAppId")
    List<ApiAppSyncSetup> findApiAppSyncSetupActiveByApiApp(@Param("apiAppId") Long apiAppId);
}

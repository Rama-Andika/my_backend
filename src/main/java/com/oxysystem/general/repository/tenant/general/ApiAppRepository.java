package com.oxysystem.general.repository.tenant.general;

import com.oxysystem.general.model.tenant.general.ApiApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiAppRepository extends JpaRepository<ApiApp, Long> {
    @Query(value = "SELECT a FROM ApiApp a INNER JOIN FETCH a.apiAppSyncSetups WHERE a.name = :name")
    Optional<ApiApp> findApiAppByName(@Param("name") String name);
}

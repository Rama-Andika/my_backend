package com.oxysystem.general.repository.master;

import com.oxysystem.general.model.master.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {
    Optional<Tenant> findByTenantIdAndStatus(String tenantId, String status);
}
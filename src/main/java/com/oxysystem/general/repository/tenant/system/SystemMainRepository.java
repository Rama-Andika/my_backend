package com.oxysystem.general.repository.tenant.system;

import com.oxysystem.general.model.tenant.system.SystemMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemMainRepository extends JpaRepository<SystemMain, Long> {
    @Query(value = "SELECT s.* FROM system_main s WHERE s.name = :name LIMIT 1", nativeQuery = true)
    Optional<SystemMain> findSystemPropertyName(@Param("name") String name);
}

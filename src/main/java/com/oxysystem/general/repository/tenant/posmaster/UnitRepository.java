package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Query("SELECT u FROM Unit u WHERE u.uomId IN :uomIds")
    List<Unit> findAllByUomIds(@Param("uomIds") List<Long> uomIds);
}

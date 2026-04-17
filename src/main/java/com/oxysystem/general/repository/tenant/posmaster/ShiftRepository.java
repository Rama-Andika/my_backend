package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    @Query(value = "SELECT s.* FROM pos_shift s ORDER BY s.start_hours LIMIT 1",nativeQuery = true)
    Optional<Shift> findFirstShift();
}

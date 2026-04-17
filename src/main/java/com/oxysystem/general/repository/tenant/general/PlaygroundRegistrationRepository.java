package com.oxysystem.general.repository.tenant.general;

import com.oxysystem.general.model.tenant.general.PlaygroundRegistration;
import com.oxysystem.general.repository.tenant.general.custom.PlaygroundRegistrationRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlaygroundRegistrationRepository extends JpaRepository<PlaygroundRegistration, Long>, PlaygroundRegistrationRepositoryCustom {
    @Query("SELECT MAX(s.counter) FROM PlaygroundRegistration s WHERE s.prefix = :prefix")
    Optional<Integer> findMaxCounter(@Param("prefix")String prefix);
}

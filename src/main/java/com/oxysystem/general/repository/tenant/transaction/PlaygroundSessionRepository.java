package com.oxysystem.general.repository.tenant.transaction;

import com.oxysystem.general.model.tenant.transaction.PlaygroundSession;
import com.oxysystem.general.repository.tenant.transaction.custom.PlaygroundSessionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaygroundSessionRepository extends JpaRepository<PlaygroundSession,Long>, PlaygroundSessionRepositoryCustom {
    @Query("SELECT s.id " +
           "FROM PlaygroundSession s " +
           "WHERE s.isActive = 1 " +
           "AND s.endTime <= CURRENT_TIMESTAMP ")
    List<Long> findExpiredActiveSessionIds();

    @Modifying
    @Query("UPDATE PlaygroundSession s " +
           "SET s.isActive = 0 " +
           "WHERE s.id IN :ids ")
    void deactivateExpiredSessions(@Param("ids") List<Long> ids);

}

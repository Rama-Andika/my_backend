package com.oxysystem.general.repository.tenant.transaction;

import com.oxysystem.general.model.tenant.transaction.PlaygroundSession;
import com.oxysystem.general.repository.tenant.transaction.custom.PlaygroundSessionReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaygroundSessionReportRepository extends JpaRepository<PlaygroundSession, Long>, PlaygroundSessionReportRepositoryCustom {
}

package com.oxysystem.general.repository.db1.transaction;

import com.oxysystem.general.model.db1.transaction.PlaygroundSession;
import com.oxysystem.general.repository.db1.transaction.custom.PlaygroundSessionReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaygroundSessionReportRepository extends JpaRepository<PlaygroundSession, Long>, PlaygroundSessionReportRepositoryCustom {
}

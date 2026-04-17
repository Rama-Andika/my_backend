package com.oxysystem.general.repository.tenant;

import com.oxysystem.general.model.tenant.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
}

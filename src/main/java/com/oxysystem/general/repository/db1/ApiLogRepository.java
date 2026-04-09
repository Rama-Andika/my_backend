package com.oxysystem.general.repository.db1;

import com.oxysystem.general.model.db1.ApiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
}

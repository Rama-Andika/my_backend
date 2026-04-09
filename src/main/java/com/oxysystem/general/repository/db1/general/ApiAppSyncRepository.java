package com.oxysystem.general.repository.db1.general;

import com.oxysystem.general.model.db1.general.ApiAppSync;
import com.oxysystem.general.repository.db1.general.custom.ApiAppSyncRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiAppSyncRepository extends JpaRepository<ApiAppSync, Long>, ApiAppSyncRepositoryCustom {
    @Modifying
    @Query("UPDATE ApiAppSync a SET a.status = :status WHERE a.apiSyncId IN :apiSyncIds")
    void updateApiAppSyncStatusByApiSyncIds(@Param("status") Integer status, @Param("apiSyncIds") List<Long> apiSyncIds);
    @Modifying
    @Query("UPDATE ApiAppSync a SET a.status = :status WHERE a.apiSyncId = :apiSyncId")
    void updateApiAppSyncStatusByApiSyncId(@Param("status") Integer status, @Param("apiSyncId") Long apiSyncId);

    @Modifying
    @Query("DELETE FROM ApiAppSync a WHERE a.status = :status AND a.tableName = :tableName AND a.ownerId = :ownerId AND a.location.locationId IN :locationIds")
    void deleteApiAppSyncByTableNameAndOwnerIdAndLocationIds(@Param("status") Integer status, @Param("tableName") String tableName, @Param("ownerId") Long ownerId, @Param("locationIds") List<Long> locationIds);
}

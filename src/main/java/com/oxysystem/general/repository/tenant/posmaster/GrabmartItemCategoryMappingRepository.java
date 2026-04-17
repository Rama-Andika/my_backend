package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.model.tenant.posmaster.GrabmartItemCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrabmartItemCategoryMappingRepository extends JpaRepository<GrabmartItemCategoryMapping, Long> {
    @Query("SELECT g FROM GrabmartItemCategoryMapping g WHERE g.grabmartItemCategoryId = :grabmartSubCategoryId")
    Optional<GrabmartItemCategoryMapping> findByGrabmartSubCategoryId(@Param("grabmartSubCategoryId") String grabmartSubCategoryId);
}

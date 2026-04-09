package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.GrabmartItemCategory;
import com.oxysystem.general.model.db1.posmaster.GrabmartItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrabmartItemCategoryRepository extends JpaRepository<GrabmartItemCategory, Long> {
    @Query("SELECT g FROM GrabmartItemCategory g WHERE g.grabmartItemCategoryId = :grabmartSubCategoryId")
    Optional<GrabmartItemCategory> findByGrabmartSubCategoryId(@Param("grabmartSubCategoryId") String grabmartSubCategoryId);

    @Query("SELECT g FROM GrabmartItemCategory g WHERE g.id IN :ids")
    List<GrabmartItemCategory> findAllGrabMartItemCategoryByIds(@Param("ids") List<Long> ids);
}

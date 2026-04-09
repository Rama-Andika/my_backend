package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.GrabfoodItemGroup;
import com.oxysystem.general.repository.db1.posmaster.custom.GrabFoodItemGroupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GrabfoodItemGroupRepository extends JpaRepository<GrabfoodItemGroup, Long>, GrabFoodItemGroupRepositoryCustom {
    @Query("SELECT g FROM GrabfoodItemGroup g WHERE g.grabfoodItemGroupId = :grabfoodId")
    Optional<GrabfoodItemGroup> findByGrabfoodId(@Param("grabfoodId") String grabfoodId);

    @Query("SELECT g FROM GrabfoodItemGroup g WHERE g.id IN :ids")
    List<GrabfoodItemGroup> findAllGrabFoodItemGroupByIds(@Param("ids") List<Long> ids);
}

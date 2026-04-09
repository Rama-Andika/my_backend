package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.GrabmartItemGroup;
import com.oxysystem.general.repository.db1.posmaster.custom.GrabMartItemGroupRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrabmartItemGroupRepository extends JpaRepository<GrabmartItemGroup, Long>, GrabMartItemGroupRepositoryCustom {
    @Query("SELECT g FROM GrabmartItemGroup g WHERE g.grabmartItemGroupId = :grabmartId")
    Optional<GrabmartItemGroup> findByGrabmartId(@Param("grabmartId") String grabmartId);

    @Query("SELECT g FROM GrabmartItemGroup g WHERE g.id IN :ids")
    List<GrabmartItemGroup> findAllGrabMartItemGroupByIds(@Param("ids") List<Long> ids);
}

package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.GrabmartItemMapping;
import com.oxysystem.general.repository.db1.posmaster.custom.GrabmartItemMappingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrabmartItemMappingRepository extends JpaRepository<GrabmartItemMapping, Long>, GrabmartItemMappingRepositoryCustom {
    @Query("SELECT ig FROM GrabmartItemMapping ig WHERE ig.itemMaster.itemMasterId = :itemMasterId")
    List<GrabmartItemMapping> findGrabmartItemMappingByItemMasterId(@Param("itemMasterId") Long itemMasterId);

    @Modifying
    @Query("DELETE FROM GrabmartItemMapping im WHERE im.itemMaster.itemMasterId = :itemId")
    void deleteGrabmartItemMappingByItemMasterId(@Param("itemId") Long itemId);
}

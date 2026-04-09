package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.GrabfoodItemMapping;
import com.oxysystem.general.repository.db1.posmaster.custom.GrabfoodItemMappingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrabfoodItemMappingRepository extends JpaRepository<GrabfoodItemMapping,Long>, GrabfoodItemMappingRepositoryCustom {
    @Query("SELECT ig FROM GrabfoodItemMapping ig WHERE ig.itemMaster.itemMasterId = :itemMasterId")
    List<GrabfoodItemMapping> findGrabfoodItemMappingByItemMasterId(@Param("itemMasterId") Long itemMasterId);

    @Modifying
    @Query("DELETE FROM GrabfoodItemMapping im WHERE im.itemMaster.itemMasterId = :itemId")
    void deleteGrabfoodItemMappingByItemMasterId(@Param("itemId") Long itemId);
}

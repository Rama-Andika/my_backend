package com.oxysystem.general.repository.db1.posmaster;

import com.oxysystem.general.model.db1.posmaster.ItemMasterImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMasterImageRepository extends JpaRepository<ItemMasterImage, Long> {
    @Query("SELECT im FROM ItemMasterImage im WHERE im.itemMaster.itemMasterId IN :itemMasterIds")
    List<ItemMasterImage> getImagesByItems(@Param("itemMasterIds") List<Long> itemMasterIds);
}

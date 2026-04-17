package com.oxysystem.general.repository.tenant.posmaster;

import com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterViewDTO;
import com.oxysystem.general.model.tenant.posmaster.ItemMaster;
import com.oxysystem.general.repository.tenant.posmaster.custom.ItemMasterRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemMasterRepository extends JpaRepository<ItemMaster, Long>, ItemMasterRepositoryCustom {
    @Query("SELECT im FROM ItemMaster im WHERE im.itemMasterId IN :itemMasterIds")
    List<ItemMaster> findItemMastersByItemMasterIds(@Param("itemMasterIds") List<Long> itemMasterIds);

    @Query(value = "SELECT im.* FROM pos_item_master im WHERE im.code = :barcode OR im.barcode = :barcode LIMIT 1", nativeQuery = true)
    Optional<ItemMaster> findItemMasterByCodeOrBarcode(@Param("barcode") String barcode);

    @Query(value = "SELECT im FROM ItemMaster im LEFT JOIN FETCH im.priceTypes WHERE im.itemMasterId IN :itemMasterIds")
    List<ItemMaster> findItemMasterWithPriceTypeByIds(@Param("itemMasterIds") List<Long> itemMasterIds);

    @Query("SELECT new com.oxysystem.general.dto.posmaster.itemMaster.view.ItemMasterViewDTO(cast(im.itemMasterId as string), " +
            "im.name, im.barcode, im.code, im.itemGroup.name, im.itemCategory.name)  " +
            "FROM ItemMaster im WHERE im.itemGroup.itemGroupId = :itemGroupId")
    List<ItemMasterViewDTO> findItemMasterByItemGroup(@Param("itemGroupId") Long itemGroupId);
}

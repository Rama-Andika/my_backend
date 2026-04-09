package com.oxysystem.general.repository.db1.transaction.stock;

import com.oxysystem.general.dto.transaction.stock.view.StockViewDTO;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.model.db1.transaction.stock.Stock;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT SUM(s.qty * s.inOut) FROM Stock s WHERE s.itemMaster.itemMasterId = :itemMasterId AND s.location.locationId = :locationId")
    Optional<Double> getCurrentStockPerItem(@Param("itemMasterId") Long itemMasterId,
                                             @Param("locationId") Long locationId);

    @Query("SELECT new com.oxysystem.general.dto.transaction.stock.view.StockViewDTO(SUM(s.qty * s.inOut), im.itemMasterId) FROM ItemMaster im " +
            "LEFT JOIN Stock s on im.itemMasterId = s.itemMaster.itemMasterId " +
            "AND s.location.locationId = :locationId " +
            "WHERE im.itemMasterId IN :itemMasterIds " +
            "GROUP BY im.itemMasterId")
    List<StockViewDTO> getCurrentStockByItems(@Param("itemMasterIds") List<Long> itemMasterIds,
                                                 @Param("locationId") Long locationId);

    @Query("SELECT s FROM Stock s WHERE s.noFaktur = :noFaktur")
    List<Stock> getRowsStockByNoFaktur(@Param("noFaktur") String noFaktur);

    @Modifying
    @Query("UPDATE Stock s SET s.status = :status WHERE s.noFaktur = :noFaktur")
    void updateStatusStockByNoFaktur(@Param("status") DocumentStatus status, @Param("noFaktur") String noFaktur);

    @Modifying
    @Query("UPDATE Stock s SET s.opnameId = :opnameId WHERE s.noFaktur = :noFaktur")
    void updateOpnameIdStockByNoFaktur(@Param("opnameId") Long opnameId, @Param("noFaktur") String noFaktur);

    @Modifying
    @Query("UPDATE Stock s SET s.date = :date WHERE s.noFaktur = :noFaktur")
    void updateDateStockByNoFaktur(@Param("date") LocalDateTime date, @Param("noFaktur") String noFaktur);

    @Modifying
    @Query("DELETE FROM Stock s WHERE s.noFaktur = :noFaktur")
    void deleteStockByNoFaktur(@Param("noFaktur") String noFaktur);

    @Modifying
    @Query("DELETE FROM Stock s WHERE s.noFaktur IN :noFakturs")
    void deleteStocksByNoFakturs(@Param("noFakturs") List<String> noFakturs);

}

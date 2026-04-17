package com.oxysystem.general.service.transaction.stock;

import com.oxysystem.general.dto.transaction.stock.view.StockViewDTO;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.model.tenant.transaction.stock.Stock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StockService {
    Stock save(Stock stock);
    List<Stock> saveAll(List<Stock> stocks);
    Optional<Double> getCurrentStockPerItem(Long itemMasterId, Long locationId);
    List<StockViewDTO> getCurrenctStockByItems(List<Long> itemMasterIds, Long locationId);
    List<Stock> getRowsStockByNoFaktur(String noFaktur);
    void updateStatusStockByNoFaktur(DocumentStatus status, String noFaktur);
    void updateOpnameIdStockByNoFaktur(Long opnameId, String noFaktur);
    void updateDateStockByNoFaktur(LocalDateTime date, String noFaktur);
    void deleteStockByNoFaktur(String noFaktur);
    void deleteStocksByNoFakturs(List<String> noFakturs);
}

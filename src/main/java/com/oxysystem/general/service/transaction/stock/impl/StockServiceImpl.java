package com.oxysystem.general.service.transaction.stock.impl;

import com.oxysystem.general.dto.transaction.stock.view.StockViewDTO;
import com.oxysystem.general.enums.DocumentStatus;
import com.oxysystem.general.model.tenant.transaction.stock.Stock;
import com.oxysystem.general.repository.tenant.transaction.stock.StockRepository;
import com.oxysystem.general.service.transaction.stock.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public List<Stock> saveAll(List<Stock> stocks) {
       return stockRepository.saveAll(stocks);
    }

    @Override
    public Optional<Double> getCurrentStockPerItem(Long itemMasterId, Long locationId) {
        return stockRepository.getCurrentStockPerItem(itemMasterId, locationId);
    }

    @Override
    public List<StockViewDTO> getCurrenctStockByItems(List<Long> itemMasterIds, Long locationId) {
        return stockRepository.getCurrentStockByItems(itemMasterIds, locationId);
    }

    @Override
    public List<Stock> getRowsStockByNoFaktur(String noFaktur) {
        return stockRepository.getRowsStockByNoFaktur(noFaktur);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public void updateStatusStockByNoFaktur(DocumentStatus status, String noFaktur) {
        stockRepository.updateStatusStockByNoFaktur(status, noFaktur);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public void updateOpnameIdStockByNoFaktur(Long opnameId, String noFaktur) {
        stockRepository.updateOpnameIdStockByNoFaktur(opnameId, noFaktur);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public void updateDateStockByNoFaktur(LocalDateTime date, String noFaktur) {
        stockRepository.updateDateStockByNoFaktur(date, noFaktur);
    }

    @Override
    @Transactional
    public void deleteStockByNoFaktur(String noFaktur) {
        stockRepository.deleteStockByNoFaktur(noFaktur);
    }

    @Override
    @Transactional( rollbackFor = {Throwable.class})
    public void deleteStocksByNoFakturs(List<String> noFakturs) {
        stockRepository.deleteStocksByNoFakturs(noFakturs);
    }
}

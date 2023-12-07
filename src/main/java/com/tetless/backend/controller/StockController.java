package com.tetless.backend.controller;

import com.tetless.backend.repository.inmemory.StockMemoryRepository;
import com.tetless.backend.service.disk.StockDiskService;
import com.tetless.backend.service.experimental.StockNotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StockController {

    private final StockMemoryRepository stockRepository;
    private final StockDiskService stockDiskService;
    private final StockNotingService stockNotingService;

    @GetMapping("/inmemory/stock/sell-count")
    public Boolean stockSellCountByInMemory() {
        return stockRepository.stockSellCount();
    }

    @GetMapping("/disk/stock/sell-count")
    public Boolean stockSellCountByDisk() {
        return stockDiskService.stockSellTransactionWithLock();
    }

    @GetMapping("/experimental/stock/sell-count")
    public Boolean stockSellCountByNoting() {
        return stockNotingService.stockSellCount();
    }
}

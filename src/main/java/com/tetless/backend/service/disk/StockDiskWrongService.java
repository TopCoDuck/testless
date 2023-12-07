package com.tetless.backend.service.disk;

import com.tetless.backend.repository.disk.StockSellDiskRepository;
import com.tetless.backend.repository.disk.entity.StockSellEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockDiskWrongService {
    private final StockSellDiskRepository stockDiskRepository;

    /*
     * 값을 가져오는 부분과 업데이트 시키는 부분 사이에 갭
     * 중복으로 동일한 값으로 업데이트
     */
    public boolean stockSellNotTransaction() {
        StockSellEntity stockSell = stockDiskRepository.findById(1).get();
        stockSell.changeSoldQty(1);
        stockDiskRepository.save(stockSell);
        return true;
    }

    /*
     * 값을 가져오는 부분과 업데이트 시키는 부분 사이에 갭
     * 중복으로 동일한 값으로 업데이트
     * Mysql은 SERIALIZABLE 제외하고 읽을 때 락을 걸지 않음
     */
    @Transactional
    public boolean stockSellTransaction() {
        StockSellEntity stockSell = stockDiskRepository.findById(1).get();
        stockSell.changeSoldQty(1);
        stockDiskRepository.save(stockSell);
        return true;
    }

    /*
     * 누군가 쓰기 전에는 읽기 락만 걸어서 동일합 값을 여러 클라이언트에서 가져오고
     * 여러 클라이언트가 쓰기 락 획득시 경쟁상태가 되어 Dead Lock 발생
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean stockSellSeralizeTransaction() {
        StockSellEntity stockSell = stockDiskRepository.findById(1).get();
        stockSell.changeSoldQty(1);
        stockDiskRepository.save(stockSell);
        return true;
    }
}

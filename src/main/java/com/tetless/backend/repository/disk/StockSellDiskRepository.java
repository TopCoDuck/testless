package com.tetless.backend.repository.disk;

import com.tetless.backend.repository.disk.entity.StockSellEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface StockSellDiskRepository extends JpaRepository<StockSellEntity, Integer> {

    Optional<StockSellEntity> findById(Integer id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    StockSellEntity findByStockSellNo(Integer id);
}

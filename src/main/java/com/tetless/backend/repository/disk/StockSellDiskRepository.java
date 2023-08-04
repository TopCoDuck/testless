package com.tetless.backend.repository.disk;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.tetless.backend.repository.disk.entity.StockSellEntity;

import jakarta.persistence.LockModeType;

public interface StockSellDiskRepository extends JpaRepository<StockSellEntity, Integer> {

	Optional<StockSellEntity> findById(Integer id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	StockSellEntity findByStockSellNo(Integer id);
}

package com.tetless.backend.repository.disk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tetless.backend.model.disk.BuyStatus;
import com.tetless.backend.repository.disk.entity.StockBuyEntity;

import jakarta.transaction.Transactional;

public interface StockBuyDiskRepository extends JpaRepository<StockBuyEntity, Integer> {

	@Query("SELECT coalesce(sum(buyQty),0) FROM StockBuyEntity WHERE stockSellNo = :stockSellNo AND buyStatus IN ('PREOCCUPY','DONE')")
	int sumBuyQuantity(@Param("stockSellNo") Integer stockSellNo);

	// 트랜잭션이 없기 때문에 다시 읽어오는 것을 방지. 속도를 높이기 위해서 수행.
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE  StockBuyEntity SET buyStatus = :buyStatus  WHERE   stockBuyNo = :stockBuyNo")
	void changeState(@Param("stockBuyNo") Integer stockBuyNo, @Param("buyStatus") BuyStatus buyStatus);
}

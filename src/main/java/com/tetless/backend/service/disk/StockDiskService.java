package com.tetless.backend.service.disk;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tetless.backend.model.disk.BuyStatus;
import com.tetless.backend.repository.disk.StockBuyDiskRepository;
import com.tetless.backend.repository.disk.StockSellDiskRepository;
import com.tetless.backend.repository.disk.entity.StockBuyEntity;
import com.tetless.backend.repository.disk.entity.StockSellEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockDiskService {

	private final StockSellDiskRepository stockDiskRepository;
	private final StockBuyDiskRepository stockBuyDiskRepository;

	@Transactional
	public boolean stockSellTransactionWithLock() {
		StockSellEntity stockSell = stockDiskRepository.findByStockSellNo(1);
		stockSell.changeSoldQty(1);
		stockDiskRepository.save(stockSell);
		return true;
	}

	public boolean stockSellCountWithStockBuy() {
		StockSellEntity stockSell = stockDiskRepository.findById(1).get();
		int preSumBuyQutity = stockBuyDiskRepository.sumBuyQuantity(1);

		if (preSumBuyQutity < stockSell.getSellQty()) {
			StockBuyEntity stockBuy = StockBuyEntity.create(1, 1);
			stockBuyDiskRepository.save(stockBuy);
			// NOTE: 실질적으로는 중간 작업으로 인한 텀이 있음
			int postSumBuyQutity = stockBuyDiskRepository.sumBuyQuantity(1);
			if (postSumBuyQutity > stockSell.getSellQty()) {
				stockBuy.changeStatus(BuyStatus.FAIL);
				log.info("overflow : {}", stockBuy.getStockBuyNo());
			} else {
				stockBuy.changeStatus(BuyStatus.DONE);
			}

			stockBuyDiskRepository.save(stockBuy);
			// NOTE: stockSell은 메시지 규에 담아서 업데이트 후 처리
		} else {
			return false;
		}

		return true;
	}
}

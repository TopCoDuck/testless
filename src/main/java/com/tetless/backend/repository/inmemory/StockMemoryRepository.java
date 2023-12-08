package com.tetless.backend.repository.inmemory;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockMemoryRepository {

	private final RedisTemplate<String, String> redisTemplate;
	private final RedisScript<Boolean> stockSellScript;
	private static final String STOCK_SELL_KEY = "stock:sell";

	public boolean stockSellCount() {
		this.stockSellCountLua();
		return true;
	}

	public void stockSellCountLua() {
		redisTemplate.execute(stockSellScript, List.of(STOCK_SELL_KEY), "");
	}

}

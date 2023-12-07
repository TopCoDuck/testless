package com.tetless.backend.repository.inmemory;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockMemoryRepository {

	private final RedisTemplate<String, String> redisTemplate;
	
	private static final String STOCK_SELL_KEY = "stock:sell";

	public boolean stockSellCount() {
		this.stockSellCountLua();
		return true;
	}

	public boolean stockSellCountLua() {
		return redisTemplate.execute(script(), List.of(STOCK_SELL_KEY), "");
	}
	
	@Bean
	RedisScript<Boolean> script() {
		  return RedisScript.of(luaScript, Boolean.class);
	}
	
	String luaScript = "local limit = redis.call('HGET', KEYS[1], 'limit')\r\n"
			+"local sold = redis.call('HGET', KEYS[1], 'sold')\r\n"
			+ "if limit == false\r\n"
			+ "  then return true\r\n"
			+ "end\r\n"
			+ "if tonumber(sold) < tonumber(limit) \r\n"
			+ "  then redis.call('HINCRBY', KEYS[1], 'sold', 1)\r\n"
			+ "  return true\r\n"
			+ "end\r\n"
			+ "return false";
}

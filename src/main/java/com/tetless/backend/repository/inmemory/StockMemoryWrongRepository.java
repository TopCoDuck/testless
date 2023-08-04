package com.tetless.backend.repository.inmemory;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StockMemoryWrongRepository {
	
	private final RedisTemplate<String, String> redisTemplate;
	
	private static final String STOCK_SELL_KEY = "stock:sell";
	private static final int STOCK_LIMT = 100;
	
	/**
	 * 레이스 컨티션 발생
	 * 값을 가져오는 부분과 증가 시키는 부분 사이에 갭으로 인하여 제한 갯수보다 더 많은 수가 카운트 됨
	 * @return
	 */
	public boolean stockSellCountCode() {
		
		int stockSellCount = Integer.parseInt(
				Optional.ofNullable(
						redisTemplate.opsForValue().get(STOCK_SELL_KEY)).orElse("0"));

		if(stockSellCount < STOCK_LIMT) {
			redisTemplate.opsForValue().increment(STOCK_SELL_KEY);
		}
		return false;
	}
	/**
	 * watch 와 multi 실행 간견의 차이로 
	 * 카운트 중에 다량의 업데이트 불가 상태 발생
	 * @return
	 */
	public boolean stockSellCountTransaction() {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
            	try {

        		int stockSellCount = Integer.parseInt(
        				Optional.ofNullable(
        						redisTemplate.opsForValue().get(STOCK_SELL_KEY)).orElse("0"));
        		if(stockSellCount <= STOCK_LIMT) {
                	operations.watch(STOCK_SELL_KEY);
        			operations.multi();
        			operations.opsForValue().increment(STOCK_SELL_KEY);
        			operations.exec();
        		} 
        		}catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        return true;
	}
}

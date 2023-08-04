package com.tetless.backend.service.experimental;

import java.io.IOException;
import java.net.InetAddress;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StockPingService {
	public boolean stockSellCount() {
		try {
			System.nanoTime();
			InetAddress inet = InetAddress.getByName("127.0.0.1");
			long startTime = System.nanoTime();
			inet.isReachable(1000);
			long endTime = System.nanoTime();
			log.info("ping nano time : {}", endTime - startTime);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}

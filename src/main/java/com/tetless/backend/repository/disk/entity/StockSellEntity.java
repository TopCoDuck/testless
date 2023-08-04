package com.tetless.backend.repository.disk.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "stock_sell")
public class StockSellEntity {
	@Id
	private int stockSellNo;

	private int sellQty;

	private int soldQty;

	public void changeSoldQty(int purchaseQty) {
		int changedSoldQty = this.soldQty + purchaseQty;
		if (changedSoldQty <= sellQty) {
			this.soldQty = changedSoldQty;
		} else {
			// throw new RuntimeException("재고가 부족합니다."); TODO: 테스트를 위해 주석 처리
		}
	}
}

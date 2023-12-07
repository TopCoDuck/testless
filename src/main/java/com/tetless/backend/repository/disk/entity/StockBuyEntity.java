package com.tetless.backend.repository.disk.entity;

import com.tetless.backend.model.disk.BuyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "stock_buy")
public class StockBuyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stockBuyNo;

    private int stockSellNo;

    private int buyQty;

    @Enumerated(EnumType.STRING)
    private BuyStatus buyStatus;

    public static StockBuyEntity create(int stockSellNo, int buyQty) {
        return StockBuyEntity.builder()
                .stockSellNo(stockSellNo)
                .buyQty(buyQty)
                .buyStatus(BuyStatus.PREOCCUPY)
                .build();
    }

    public void changeStatus(BuyStatus buyStatus) {
        this.buyStatus = buyStatus;
    }
}

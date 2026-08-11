package com.wooyong.trading_backtest.backtest;

import java.time.LocalDate;

public class StockPrice {

    // TODO: 날짜
    private final LocalDate date;

    // TODO: 종가(센트 단위)
    private final int closePriceCents;

    // TODO: 모든 필드를 받는 생성자
    public StockPrice(LocalDate date, int closePriceCents){
        this.date = date;
        this.closePriceCents = closePriceCents;
    }

    // TODO: Getter 2개

    public LocalDate getDate() {
        return date;
    }

    public int getClosePriceCents() {
        return closePriceCents;
    }
}

package com.wooyong.trading_backtest.backtest;

import java.time.LocalDate;

public class BacktestTrade {

    private final LocalDate date;
    private final TradeSignal signal;
    private final int priceCents;
    private final long quantity;

    public BacktestTrade(
            LocalDate date,
            TradeSignal signal,
            int priceCents,
            long quantity
    ) {
        this.date = date;
        this.signal = signal;
        this.priceCents = priceCents;
        this.quantity = quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public TradeSignal getSignal() {
        return signal;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public long getQuantity() {
        return quantity;
    }
}
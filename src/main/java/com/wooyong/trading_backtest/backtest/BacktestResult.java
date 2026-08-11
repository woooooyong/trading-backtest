package com.wooyong.trading_backtest.backtest;

import java.util.List;

public class BacktestResult {

    private final double totalReturnPercent;
    private final int tradeCount;
    private final double winRate;
    private final double mddPercent;
    private final List<BacktestTrade> trades;

    public BacktestResult(
            double totalReturnPercent,
            int tradeCount,
            double winRate,
            double mddPercent,
            List<BacktestTrade> trades
    ) {
        this.totalReturnPercent = totalReturnPercent;
        this.tradeCount = tradeCount;
        this.winRate = winRate;
        this.mddPercent = mddPercent;
        this.trades = List.copyOf(trades);
    }

    public double getTotalReturnPercent() {
        return totalReturnPercent;
    }

    public int getTradeCount() {
        return tradeCount;
    }

    public double getWinRate() {
        return winRate;
    }

    public double getMddPercent() {
        return mddPercent;
    }

    public List<BacktestTrade> getTrades() {
        return trades;
    }
}
package com.wooyong.trading_backtest.backtest.dto;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BacktestExecutionResponse {

    private final Long id;
    private final int shortPeriod;
    private final int longPeriod;
    private final long initialCash;
    private final double totalReturnPercent;
    private final int tradeCount;
    private final double winRate;
    private final double mddPercent;
    private final LocalDateTime createdAt;
    private final Long strategyId;
    private final String symbol;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public BacktestExecutionResponse(
            Long id,
            Long strategyId,
            String symbol,
            LocalDate startDate,
            LocalDate endDate,
            int shortPeriod,
            int longPeriod,
            long initialCash,
            double totalReturnPercent,
            int tradeCount,
            double winRate,
            double mddPercent,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.strategyId = strategyId;
        this.symbol = symbol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
        this.initialCash = initialCash;
        this.totalReturnPercent = totalReturnPercent;
        this.tradeCount = tradeCount;
        this.winRate = winRate;
        this.mddPercent = mddPercent;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getShortPeriod() {
        return shortPeriod;
    }

    public int getLongPeriod() {
        return longPeriod;
    }

    public long getInitialCash() {
        return initialCash;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
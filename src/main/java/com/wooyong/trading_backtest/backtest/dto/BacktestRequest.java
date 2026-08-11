package com.wooyong.trading_backtest.backtest.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class BacktestRequest {


    @Positive
    private long initialCash;

    @NotNull
    @Positive
    private Long strategyId;

    @NotBlank
    private String symbol;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public long getInitialCash() {
        return initialCash;
    }

    public void setInitialCash(long initialCash) {
        this.initialCash = initialCash;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }
}
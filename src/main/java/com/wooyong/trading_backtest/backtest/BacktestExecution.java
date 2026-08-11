package com.wooyong.trading_backtest.backtest;

import com.wooyong.trading_backtest.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.wooyong.trading_backtest.strategy.Strategy;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "backtest_results")
public class BacktestExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "short_period")
    private int shortPeriod;

    @Column(name = "long_period")
    private int longPeriod;

    @Column(name = "initial_cash")
    private long initialCash;

    @Column(name = "total_return_percent")
    private double totalReturnPercent;

    @Column(name = "trade_count")
    private int tradeCount;

    @Column(name = "win_rate")
    private double winRate;

    @Column(name = "mdd_percent")
    private double mddPercent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    protected BacktestExecution() {
    }

    public BacktestExecution(
            Member member,
            Strategy strategy,
            String symbol,
            LocalDate startDate,
            LocalDate endDate,
            int shortPeriod,
            int longPeriod,
            long initialCash,
            BacktestResult result

    ) {
        this.member = member;
        this.strategy = strategy;
        this.symbol = symbol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
        this.initialCash = initialCash;
        this.totalReturnPercent = result.getTotalReturnPercent();
        this.tradeCount = result.getTradeCount();
        this.winRate = result.getWinRate();
        this.mddPercent = result.getMddPercent();
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
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

    public Long getStrategyId() {
        if (strategy == null) {
            return null;
        }

        return strategy.getId();
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
}
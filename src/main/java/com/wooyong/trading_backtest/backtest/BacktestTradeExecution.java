package com.wooyong.trading_backtest.backtest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "backtest_trades")
public class BacktestTradeExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backtest_result_id", nullable = false)
    private BacktestExecution backtestExecution;

    @Column(name = "trade_date", nullable = false)
    private LocalDate tradeDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "trade_type", nullable = false)
    private TradeSignal tradeSignal;

    @Column(name = "price_cents", nullable = false)
    private int priceCents;

    @Column(nullable = false)
    private long quantity;

    protected BacktestTradeExecution() {
    }

    public BacktestTradeExecution(
            BacktestExecution backtestExecution,
            BacktestTrade backtestTrade
    ) {
        this.backtestExecution = backtestExecution;
        this.tradeDate = backtestTrade.getDate();
        this.tradeSignal = backtestTrade.getSignal();
        this.priceCents = backtestTrade.getPriceCents();
        this.quantity = backtestTrade.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public TradeSignal getTradeSignal() {
        return tradeSignal;
    }

    public int getPriceCents() {
        return priceCents;
    }

    public long getQuantity() {
        return quantity;
    }
}
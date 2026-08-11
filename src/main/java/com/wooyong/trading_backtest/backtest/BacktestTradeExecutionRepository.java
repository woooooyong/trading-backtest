package com.wooyong.trading_backtest.backtest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BacktestTradeExecutionRepository
        extends JpaRepository<BacktestTradeExecution, Long> {

    List<BacktestTradeExecution>
    findByBacktestExecution_IdOrderByIdAsc(
            Long backtestExecutionId
    );
}
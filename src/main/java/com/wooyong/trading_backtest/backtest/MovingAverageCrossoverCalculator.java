package com.wooyong.trading_backtest.backtest;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MovingAverageCrossoverCalculator {
    private final MovingAverageCalculator movingAverageCalculator;

    public MovingAverageCrossoverCalculator(
            MovingAverageCalculator movingAverageCalculator
    ) {
        this.movingAverageCalculator = movingAverageCalculator;
    }

    public TradeSignal calculate(
            List<Integer> closingPrices,
            int endIndex,
            int shortPeriod,
            int longPeriod
    ) {
        double todayShort = movingAverageCalculator.calculate(
                closingPrices, endIndex, shortPeriod
        );
        double todayLong = movingAverageCalculator.calculate(
                closingPrices, endIndex, longPeriod
        );

        double yesterdayShort = movingAverageCalculator.calculate(
                closingPrices, endIndex - 1, shortPeriod
        );
        double yesterdayLong = movingAverageCalculator.calculate(
                closingPrices, endIndex - 1, longPeriod
        );

        if (yesterdayShort <= yesterdayLong
                && todayShort > todayLong) {
            return TradeSignal.BUY;
        }

        if (yesterdayShort >= yesterdayLong
                && todayShort < todayLong) {
            return TradeSignal.SELL;
        }

        return TradeSignal.HOLD;
    }
}
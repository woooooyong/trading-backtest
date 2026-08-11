package com.wooyong.trading_backtest.backtest;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.stream.IntStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MovingAverageCalculatorTest {
    @Test
    void 최근_5개_가격의_이동평균을_계산한다() {
        MovingAverageCalculator calculator =
                new MovingAverageCalculator();

        List<Integer> prices = List.of(10, 20, 30, 40, 50);

        double result = calculator.calculate(prices, 4, 5);

        assertEquals(30.0, result);
    }

    @Test
    void 가격_데이터가_부족하면_예외가_발생한다() {
        MovingAverageCalculator calculator =
                new MovingAverageCalculator();

        List<Integer> prices = List.of(10, 20, 30);

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculate(prices, 2, 5)
        );
    }
    @Test
    void 단기선이_장기선을_위로_돌파하면_BUY를_반환한다() {
        MovingAverageCalculator movingAverageCalculator =
                new MovingAverageCalculator();

        MovingAverageCrossoverCalculator crossoverCalculator =
                new MovingAverageCrossoverCalculator(movingAverageCalculator);

        List<Integer> prices = List.of(10, 10, 10, 10, 10, 10, 20);

        TradeSignal result = crossoverCalculator.calculate(
                prices, 6, 2, 3
        );

        assertEquals(TradeSignal.BUY, result);
    }

    @Test
    void 단기선이_장기선을_아래로_돌파하면_SELL을_반환한다() {
        MovingAverageCalculator movingAverageCalculator =
                new MovingAverageCalculator();

        MovingAverageCrossoverCalculator crossoverCalculator =
                new MovingAverageCrossoverCalculator(movingAverageCalculator);

        List<Integer> prices = List.of(20, 20, 20, 20, 20, 20, 10);

        TradeSignal result = crossoverCalculator.calculate(
                prices, 6, 2, 3
        );

        assertEquals(TradeSignal.SELL, result);
    }

    @Test
    void 교차가_없으면_HOLD를_반환한다() {
        MovingAverageCalculator movingAverageCalculator =
                new MovingAverageCalculator();

        MovingAverageCrossoverCalculator crossoverCalculator =
                new MovingAverageCrossoverCalculator(movingAverageCalculator);

        List<Integer> prices = List.of(10, 10, 10, 10, 10, 10, 10);

        TradeSignal result = crossoverCalculator.calculate(
                prices, 6, 2, 3
        );

        assertEquals(TradeSignal.HOLD, result);
    }

    @Test
    void 매수_후_매도하면_수익률과_거래횟수를_계산한다() {
        MovingAverageCalculator movingAverageCalculator =
                new MovingAverageCalculator();

        MovingAverageCrossoverCalculator crossoverCalculator =
                new MovingAverageCrossoverCalculator(movingAverageCalculator);

        BacktestEngine backtestEngine =
                new BacktestEngine(crossoverCalculator);

        List<Integer> prices = List.of(
                10, 10, 10, 10, 10, 10,
                20, 30, 30, 30, 30, 25
        );

        List<StockPrice> stockPrices = IntStream
                .range(0, prices.size())
                .mapToObj(index -> new StockPrice(
                        LocalDate.of(2026, 1, 1).plusDays(index),
                        prices.get(index)
                ))
                .toList();

        BacktestResult result = backtestEngine.run(
                stockPrices, 2, 3, 1_000_000
        );

        assertEquals(25.0, result.getTotalReturnPercent());
        assertEquals(1, result.getTradeCount());
        assertEquals(100.0, result.getWinRate());
        assertEquals(
                16.6667,
                result.getMddPercent(),
                0.0001
        );
        assertEquals(2, result.getTrades().size());

        BacktestTrade buyTrade = result.getTrades().get(0);

        assertEquals(TradeSignal.BUY, buyTrade.getSignal());
        assertEquals(LocalDate.of(2026, 1, 7), buyTrade.getDate());
        assertEquals(20, buyTrade.getPriceCents());
        assertEquals(50_000, buyTrade.getQuantity());

        BacktestTrade sellTrade = result.getTrades().get(1);

        assertEquals(TradeSignal.SELL, sellTrade.getSignal());
        assertEquals(LocalDate.of(2026, 1, 12), sellTrade.getDate());
        assertEquals(25, sellTrade.getPriceCents());
        assertEquals(50_000, sellTrade.getQuantity());
    }
}

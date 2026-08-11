package com.wooyong.trading_backtest.backtest;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class BacktestEngine {
    private final MovingAverageCrossoverCalculator crossoverCalculator;

    public BacktestEngine(
            MovingAverageCrossoverCalculator crossoverCalculator
    ) {
        this.crossoverCalculator = crossoverCalculator;
    }

    public BacktestResult run(
            List<StockPrice> stockPrices,
            int shortPeriod,
            int longPeriod,
            long initialCash
    ) {
        List<Integer> closingPrices = stockPrices.stream()
                .map(StockPrice::getClosePriceCents)
                .toList();

        List<BacktestTrade> trades = new ArrayList<>();

        long cash = initialCash;
        long quantity = 0;
        long buyPrice = 0;

        int tradeCount = 0;
        int winningTradeCount = 0;

        long peakAsset = initialCash;
        double mddPercent = 0;

        for (int i = longPeriod; i < stockPrices.size(); i++) {
            int currentPrice = stockPrices.get(i).getClosePriceCents();

            TradeSignal signal = crossoverCalculator.calculate(
                    closingPrices,
                    i,
                    shortPeriod,
                    longPeriod
            );

            if (signal == TradeSignal.BUY && quantity == 0) {
                quantity = cash / currentPrice;
                cash = cash % currentPrice;
                buyPrice = currentPrice;

                if (quantity > 0) {
                    trades.add(new BacktestTrade(
                            stockPrices.get(i).getDate(),
                            TradeSignal.BUY,
                            currentPrice,
                            quantity
                    ));
                }
            }

            if (signal == TradeSignal.SELL && quantity > 0) {
                if (currentPrice > buyPrice) {
                    winningTradeCount++;
                }

                trades.add(new BacktestTrade(
                        stockPrices.get(i).getDate(),
                        TradeSignal.SELL,
                        currentPrice,
                        quantity
                ));

                cash = cash + quantity * currentPrice;
                quantity = 0;
                tradeCount++;
            }

            long currentAsset = cash + quantity * currentPrice;

            if (currentAsset > peakAsset) {
                peakAsset = currentAsset;
            } else {
                double drawdownPercent =
                        (double) (peakAsset - currentAsset)
                                / peakAsset * 100;

                if (drawdownPercent > mddPercent) {
                    mddPercent = drawdownPercent;
                }
            }
        }

        int lastPrice = stockPrices
                .get(stockPrices.size() - 1)
                .getClosePriceCents();

        long finalAsset = cash + quantity * lastPrice;

        double totalReturnPercent =
                (double) (finalAsset - initialCash) / initialCash * 100;

        double winRate;

        if (tradeCount == 0) {
            winRate = 0;
        } else {
            winRate = (double) winningTradeCount / tradeCount * 100;
        }

        return new BacktestResult(
                totalReturnPercent,
                tradeCount,
                winRate,
                mddPercent,
                trades
        );
    }
}
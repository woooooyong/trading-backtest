package com.wooyong.trading_backtest.backtest;

import java.time.LocalDate;
import java.util.List;

public interface StockPriceProvider {

    List<StockPrice> getDailyPrices(
            String symbol,
            LocalDate startDate,
            LocalDate endDate
    );
}
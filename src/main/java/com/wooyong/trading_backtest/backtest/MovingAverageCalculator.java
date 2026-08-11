package com.wooyong.trading_backtest.backtest;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class MovingAverageCalculator {
    public double calculate(
            List<Integer> closingPrices,
            int endIndex,
            int period
    ) {
        int sum = 0;

        int startIndex = endIndex - period + 1;

        if (startIndex < 0) {
            throw new IllegalArgumentException(
                    "이동평균선을 계산하기 위한 가격 데이터가 부족합니다."
            );
        }

        for (int i = startIndex; i <= endIndex; i++) {
            sum += closingPrices.get(i);
        }

        return sum / (double) period;
    }


}

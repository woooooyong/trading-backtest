package com.wooyong.trading_backtest.backtest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

import java.time.LocalDate;
import java.util.List;

@Component
public class AlphaVantageStockPriceProvider
        implements StockPriceProvider {

    private final RestClient restClient;
    private final String apiKey;

    public AlphaVantageStockPriceProvider(
            @Value("${alpha-vantage.api-key}") String apiKey
    ) {
        this.restClient = RestClient.create(
                "https://www.alphavantage.co"
        );

        this.apiKey = apiKey;
    }

    @Override
    public List<StockPrice> getDailyPrices(
            String symbol,
            LocalDate startDate,
            LocalDate endDate
    ) {
        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", "TIME_SERIES_DAILY")
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build()
                )
                .retrieve()
                .body(JsonNode.class);

        JsonNode timeSeries = response == null
                ? null
                : response.get("Time Series (Daily)");

        if (timeSeries == null) {
            throw new IllegalArgumentException(
                    "주가 데이터를 가져오지 못했습니다."
            );
        }

        List<StockPrice> stockPrices = new ArrayList<>();

        for (Map.Entry<String, JsonNode> entry : timeSeries.properties()) {
            LocalDate date = LocalDate.parse(entry.getKey());

            if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                String closeText = entry.getValue()
                        .get("4. close")
                        .asString();

                int closePriceCents = new BigDecimal(closeText)
                        .movePointRight(2)
                        .intValueExact();

                stockPrices.add(
                        new StockPrice(date, closePriceCents)
                );
            }
        }

        if (stockPrices.isEmpty()) {
            throw new IllegalArgumentException(
                    "요청한 기간에 주가 데이터가 없습니다."
            );
        }

        stockPrices.sort(
                Comparator.comparing(StockPrice::getDate)
        );

        return stockPrices;
    }
}
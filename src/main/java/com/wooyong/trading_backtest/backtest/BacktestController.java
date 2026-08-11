package com.wooyong.trading_backtest.backtest;

import com.wooyong.trading_backtest.backtest.dto.BacktestRequest;
import com.wooyong.trading_backtest.backtest.dto.BacktestTradeResponse;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.wooyong.trading_backtest.backtest.dto.BacktestExecutionResponse;

import java.util.List;

@RestController
@RequestMapping("/backtests")
public class BacktestController {

    private final BacktestService backtestService;

    public BacktestController(BacktestService backtestService) {
        this.backtestService = backtestService;
    }

    @PostMapping("/run")
    public BacktestResult run(
            @Valid @RequestBody BacktestRequest request
    ) {
        return backtestService.run(request);
    }

    @GetMapping
    public List<BacktestExecutionResponse> findMyExecutions() {
        return backtestService.findMyExecutions();
    }

    @GetMapping("/{executionId}/trades")
    public List<BacktestTradeResponse> getMyTrades(
            @PathVariable Long executionId
    ) {
        return backtestService.findMyTrades(executionId);
    }
}
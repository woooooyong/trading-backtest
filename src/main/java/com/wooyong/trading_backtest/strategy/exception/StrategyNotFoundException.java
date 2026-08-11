package com.wooyong.trading_backtest.strategy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class StrategyNotFoundException extends RuntimeException {

    public StrategyNotFoundException(Long id) {
        super("전략을 찾을 수 없습니다. id: " + id);
    }
}
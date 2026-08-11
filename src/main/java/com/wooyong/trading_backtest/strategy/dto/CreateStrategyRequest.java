package com.wooyong.trading_backtest.strategy.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class CreateStrategyRequest {

    // TODO: name, shortPeriod, longPeriod 필드를 private으로 선언
    @NotBlank
    private String name;
    @Positive
    private int shortPeriod;
    @Positive
    private int longPeriod;

    // TODO: 각 필드 Getter
    public String getName(){
        return name;
    }
    public int getShortPeriod() {
        return shortPeriod;
    }
    public int getLongPeriod() {
        return longPeriod;
    }

    // TODO: 각 필드 Setter
    public void setName(String name) {
        this.name = name;
    }
    public void setShortPeriod(int shortPeriod) {
        this.shortPeriod = shortPeriod;
    }
    public void setLongPeriod(int longPeriod) {
        this.longPeriod = longPeriod;
    }
}
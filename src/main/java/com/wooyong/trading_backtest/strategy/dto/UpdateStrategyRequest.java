package com.wooyong.trading_backtest.strategy.dto;
import jakarta.validation.constraints.NotBlank;

public class UpdateStrategyRequest {
    @NotBlank
    private String name;

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

}

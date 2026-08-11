package com.wooyong.trading_backtest.strategy;

import com.wooyong.trading_backtest.strategy.dto.CreateStrategyRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.wooyong.trading_backtest.strategy.dto.UpdateStrategyRequest;
import org.springframework.web.bind.annotation.PatchMapping;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/strategies")
public class StrategyController {

    private final StrategyService strategyService;

    public StrategyController(StrategyService strategyService) {
        this.strategyService = strategyService;
    }
    @GetMapping
    public List<Strategy> findAll() {
        return strategyService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Strategy create(@Valid @RequestBody CreateStrategyRequest request) {
        return strategyService.create(request);
    }
    @GetMapping("/{id}")
    public Strategy findById(@PathVariable Long id) {
        return strategyService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        strategyService.deleteById(id);
    }
    @PatchMapping("/{id}")
    public Strategy updateName(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStrategyRequest request
    ) {
        // TODO: Service의 updateName 호출 결과 반환
        return strategyService.updateName(id, request);
    }
}
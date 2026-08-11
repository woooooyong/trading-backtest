package com.wooyong.trading_backtest.backtest;

import com.wooyong.trading_backtest.backtest.dto.BacktestRequest;
import com.wooyong.trading_backtest.member.MemberRepository;
import com.wooyong.trading_backtest.strategy.StrategyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import com.wooyong.trading_backtest.member.Member;
import com.wooyong.trading_backtest.strategy.Strategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class BacktestServiceTest {

    @Mock
    private BacktestEngine backtestEngine;

    @Mock
    private BacktestExecutionRepository backtestExecutionRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StrategyRepository strategyRepository;

    @Mock
    private StockPriceProvider stockPriceProvider;

    @Mock
    private BacktestTradeExecutionRepository
            backtestTradeExecutionRepository;

    @InjectMocks
    private BacktestService backtestService;

    @Test
    void 시작일이_종료일보다_빠르지_않으면_예외가_발생한다() {
        BacktestRequest request = new BacktestRequest();
        request.setStartDate(LocalDate.of(2026, 8, 1));
        request.setEndDate(LocalDate.of(2026, 5, 1));

        assertThrows(
                IllegalArgumentException.class,
                () -> backtestService.run(request)
        );

        verifyNoInteractions(
                backtestEngine,
                backtestExecutionRepository,
                memberRepository,
                strategyRepository,
                stockPriceProvider
        );
    }

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "test@example.com",
                        null
                )
        );
    }

    @Test
    void 정상_요청이면_주가로_백테스트하고_결과를_저장한다() {
        LocalDate startDate = LocalDate.of(2026, 5, 1);
        LocalDate endDate = LocalDate.of(2026, 5, 10);

        BacktestRequest request = new BacktestRequest();
        request.setStrategyId(7L);
        request.setSymbol("AAPL");
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setInitialCash(1_000_000);

        Member member = new Member(
                "test@example.com",
                "encoded-password"
        );

        Strategy strategy = new Strategy(
                "테스트 전략",
                2,
                3,
                member
        );

        List<StockPrice> stockPrices = List.of(
                new StockPrice(LocalDate.of(2026, 5, 1), 1000),
                new StockPrice(LocalDate.of(2026, 5, 2), 1100),
                new StockPrice(LocalDate.of(2026, 5, 5), 1200),
                new StockPrice(LocalDate.of(2026, 5, 6), 1300)
        );

        BacktestResult expectedResult = new BacktestResult(
                1.0,
                1,
                100.0,
                5.0,
                List.of()
        );

        when(memberRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(member));

        when(strategyRepository.findByIdAndMember(7L, member))
                .thenReturn(Optional.of(strategy));

        when(stockPriceProvider.getDailyPrices(
                "AAPL",
                startDate,
                endDate
        )).thenReturn(stockPrices);

        when(backtestEngine.run(
                stockPrices,
                2,
                3,
                1_000_000
        )).thenReturn(expectedResult);

        BacktestResult actualResult = backtestService.run(request);

        assertSame(expectedResult, actualResult);

        verify(backtestEngine).run(
                stockPrices,
                2,
                3,
                1_000_000
        );

        verify(backtestExecutionRepository)
                .save(any(BacktestExecution.class));

        verify(backtestTradeExecutionRepository)
                .saveAll(List.of());
    }
}
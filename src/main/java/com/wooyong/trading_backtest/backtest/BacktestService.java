package com.wooyong.trading_backtest.backtest;

import com.wooyong.trading_backtest.backtest.dto.BacktestRequest;
import com.wooyong.trading_backtest.backtest.dto.BacktestTradeResponse;
import com.wooyong.trading_backtest.member.Member;
import com.wooyong.trading_backtest.member.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.wooyong.trading_backtest.backtest.dto.BacktestExecutionResponse;
import com.wooyong.trading_backtest.strategy.Strategy;
import com.wooyong.trading_backtest.strategy.StrategyRepository;
import com.wooyong.trading_backtest.strategy.exception.StrategyNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
public class BacktestService {

    private final BacktestEngine backtestEngine;
    private final BacktestExecutionRepository backtestExecutionRepository;
    private final MemberRepository memberRepository;
    private final StrategyRepository strategyRepository;
    private final StockPriceProvider stockPriceProvider;
    private final BacktestTradeExecutionRepository
            backtestTradeExecutionRepository;

    public BacktestService(
            BacktestEngine backtestEngine,
            BacktestExecutionRepository backtestExecutionRepository,
            MemberRepository memberRepository,
            StrategyRepository strategyRepository,
            StockPriceProvider stockPriceProvider,
            BacktestTradeExecutionRepository backtestTradeExecutionRepository
    ) {
        this.backtestEngine = backtestEngine;
        this.backtestExecutionRepository = backtestExecutionRepository;
        this.memberRepository = memberRepository;
        this.strategyRepository = strategyRepository;
        this.stockPriceProvider = stockPriceProvider;
        this.backtestTradeExecutionRepository =
                backtestTradeExecutionRepository;
    }

    @Transactional
    public BacktestResult run(BacktestRequest request) {

        if (!request.getStartDate().isBefore(request.getEndDate())) {
            throw new IllegalArgumentException(
                    "시작일은 종료일보다 빨라야 합니다."
            );
        }
        Member member = getCurrentMember();

        Strategy strategy = strategyRepository
                .findByIdAndMember(request.getStrategyId(), member)
                .orElseThrow(() -> new StrategyNotFoundException(
                        request.getStrategyId()
                ));

        List<StockPrice> stockPrices = stockPriceProvider
                .getDailyPrices(
                        request.getSymbol(),
                        request.getStartDate(),
                        request.getEndDate()
                );

        if (stockPrices.size() <= strategy.getLongPeriod()) {
            throw new IllegalArgumentException(
                    "이동평균선을 계산하기 위한 가격 데이터가 부족합니다."
            );
        }

        BacktestResult result = backtestEngine.run(
                stockPrices,
                strategy.getShortPeriod(),
                strategy.getLongPeriod(),
                request.getInitialCash()
        );

        BacktestExecution execution = new BacktestExecution(
                member,
                strategy,
                request.getSymbol(),
                request.getStartDate(),
                request.getEndDate(),
                strategy.getShortPeriod(),
                strategy.getLongPeriod(),
                request.getInitialCash(),
                result
        );

        backtestExecutionRepository.save(execution);

        List<BacktestTradeExecution> tradeExecutions = result.getTrades()
                .stream()
                .map(trade -> new BacktestTradeExecution(
                        execution,
                        trade
                ))
                .toList();

        backtestTradeExecutionRepository.saveAll(tradeExecutions);

        return result;
    }

    private Member getCurrentMember() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "로그인한 회원을 찾을 수 없습니다."
                ));
    }

    public List<BacktestExecutionResponse> findMyExecutions() {
        Member member = getCurrentMember();

        return backtestExecutionRepository
                .findAllByMemberOrderByCreatedAtDesc(member)
                .stream()
                .map(execution -> new BacktestExecutionResponse(
                        execution.getId(),
                        execution.getStrategyId(),
                        execution.getSymbol(),
                        execution.getStartDate(),
                        execution.getEndDate(),
                        execution.getShortPeriod(),
                        execution.getLongPeriod(),
                        execution.getInitialCash(),
                        execution.getTotalReturnPercent(),
                        execution.getTradeCount(),
                        execution.getWinRate(),
                        execution.getMddPercent(),
                        execution.getCreatedAt()
                ))
                .toList();
    }

    public List<BacktestTradeResponse> findMyTrades(
            Long executionId
    ) {
        Member member = getCurrentMember();

        backtestExecutionRepository
                .findByIdAndMember(executionId, member)
                .orElseThrow(() -> new IllegalArgumentException(
                        "백테스트 실행 기록을 찾을 수 없습니다."
                ));

        return backtestTradeExecutionRepository
                .findByBacktestExecution_IdOrderByIdAsc(executionId)
                .stream()
                .map(trade -> new BacktestTradeResponse(
                        trade.getTradeDate(),
                        trade.getTradeSignal(),
                        trade.getPriceCents(),
                        trade.getQuantity()
                ))
                .toList();
    }
}
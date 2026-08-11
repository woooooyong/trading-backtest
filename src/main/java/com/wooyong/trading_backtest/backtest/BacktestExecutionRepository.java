package com.wooyong.trading_backtest.backtest;

import com.wooyong.trading_backtest.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BacktestExecutionRepository
        extends JpaRepository<BacktestExecution, Long> {

    // TODO: 특정 회원의 결과를 createdAt 내림차순으로 조회
    List<BacktestExecution> findAllByMemberOrderByCreatedAtDesc(
            Member member
    );

    Optional<BacktestExecution> findByIdAndMember(
            Long id,
            Member member
    );
}
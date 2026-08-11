package com.wooyong.trading_backtest.strategy;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wooyong.trading_backtest.member.Member;

import java.util.List;
import java.util.Optional;


public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    boolean existsByName(String name);

    List<Strategy> findAllByMember(Member member);

    Optional<Strategy> findByIdAndMember(Long id, Member member);

    boolean existsByNameAndMember(String name, Member member);
}

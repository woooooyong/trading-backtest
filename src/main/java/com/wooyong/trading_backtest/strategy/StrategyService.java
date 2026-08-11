package com.wooyong.trading_backtest.strategy;

import com.wooyong.trading_backtest.member.MemberRepository;
import com.wooyong.trading_backtest.strategy.dto.CreateStrategyRequest;
import com.wooyong.trading_backtest.strategy.exception.StrategyNotFoundException;
import org.springframework.stereotype.Service;
import com.wooyong.trading_backtest.strategy.dto.UpdateStrategyRequest;
import com.wooyong.trading_backtest.member.Member;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;


@Service
public class StrategyService {


    public Strategy create(CreateStrategyRequest request) {
        if (request.getShortPeriod() >= request.getLongPeriod()) {
            throw new IllegalArgumentException(
                    "단기 기간은 장기 기간보다 작아야 합니다."
            );
        }

        Member member = getCurrentMember();

        if (strategyRepository.existsByNameAndMember(
                request.getName(),
                member
        )) {
            throw new IllegalArgumentException(
                    "이미 존재하는 전략 이름입니다."
            );
        }

        Strategy strategy = new Strategy(
                request.getName(),
                request.getShortPeriod(),
                request.getLongPeriod(),
                member
        );

        return strategyRepository.save(strategy);
    }

    public List<Strategy> findAll() {
        Member member = getCurrentMember();

        return strategyRepository.findAllByMember(member);
    }

    public Strategy findById(Long id) {
        Member member = getCurrentMember();

        Strategy strategy = strategyRepository
                .findByIdAndMember(id, member)
                .orElse(null);

        if (strategy == null) {
            throw new StrategyNotFoundException(id);
        }

        return strategy;
    }

    public void deleteById(Long id) {
        Strategy strategy = findById(id);
        strategyRepository.deleteById(id);
    }

    public Strategy updateName(Long id, UpdateStrategyRequest request) {
        // TODO: findById(id)로 기존 전략 찾기
        Strategy findStrategy = findById(id);
        if (!findStrategy.getName().equals(request.getName())
                && strategyRepository.existsByNameAndMember(
                request.getName(),
                findStrategy.getMember()
        )) {
            throw new IllegalArgumentException(
                    "이미 존재하는 전략 이름입니다."
            );
        }

        findStrategy.changeName(request.getName());
        return strategyRepository.save(findStrategy);
    }

    private final StrategyRepository strategyRepository;
    private final MemberRepository memberRepository;
    public StrategyService(StrategyRepository strategyRepository, MemberRepository memberRepository) {
        this.strategyRepository = strategyRepository;
        this.memberRepository = memberRepository;
    }

    private Member getCurrentMember() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        Member member = memberRepository.findByEmail(email)
                .orElse(null);

        if (member == null) {
            throw new IllegalArgumentException(
                    "로그인한 회원을 찾을 수 없습니다."
            );
        }

        return member;
    }
}

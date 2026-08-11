package com.wooyong.trading_backtest.strategy;

import com.wooyong.trading_backtest.member.MemberRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wooyong.trading_backtest.strategy.dto.CreateStrategyRequest;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verifyNoInteractions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.wooyong.trading_backtest.member.Member;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import java.util.Optional;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class StrategyServiceTest {

    @Mock
    private StrategyRepository strategyRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private StrategyService strategyService;

    @Test
    void 단기_기간이_장기_기간보다_크거나_같으면_예외가_발생한다() {
        CreateStrategyRequest request = new CreateStrategyRequest();
        request.setName("잘못된 전략");
        request.setShortPeriod(20);
        request.setLongPeriod(5);

        assertThrows(
                IllegalArgumentException.class,
                () -> strategyService.create(request)
        );

        verifyNoInteractions(strategyRepository, memberRepository);
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
    void 같은_회원의_전략_이름이_중복되면_예외가_발생한다() {
        CreateStrategyRequest request = new CreateStrategyRequest();
        request.setName("중복 전략");
        request.setShortPeriod(5);
        request.setLongPeriod(20);

        Member member = new Member(
                "test@example.com",
                "encoded-password"
        );

        when(memberRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(member));

        when(strategyRepository.existsByNameAndMember("중복 전략", member))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> strategyService.create(request)
        );

        verify(strategyRepository, never())
                .save(any(Strategy.class));
    }

    @Test
    void 정상_입력이면_현재_회원에게_연결된_전략을_저장한다() {
        CreateStrategyRequest request = new CreateStrategyRequest();
        request.setName("정상 전략");
        request.setShortPeriod(5);
        request.setLongPeriod(20);

        Member member = new Member(
                "test@example.com",
                "encoded-password"
        );

        when(memberRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(member));

        when(strategyRepository.existsByNameAndMember("정상 전략", member))
                .thenReturn(false);

        strategyService.create(request);

        ArgumentCaptor<Strategy> captor =
                ArgumentCaptor.forClass(Strategy.class);

        verify(strategyRepository).save(captor.capture());

        Strategy savedStrategy = captor.getValue();

        assertEquals("정상 전략", savedStrategy.getName());
        assertEquals(5, savedStrategy.getShortPeriod());
        assertEquals(20, savedStrategy.getLongPeriod());
        assertSame(member, savedStrategy.getMember());
    }
}

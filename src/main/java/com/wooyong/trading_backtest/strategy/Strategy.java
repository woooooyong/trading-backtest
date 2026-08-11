package com.wooyong.trading_backtest.strategy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wooyong.trading_backtest.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "strategies")
public class Strategy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "short_period")
    private int shortPeriod;

    @Column(name = "long_period")
    private int longPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Strategy() {
    }

    public Strategy(String name, int shortPeriod, int longPeriod) {
        this.name = name;
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
    }

    public Strategy(
            String name,
            int shortPeriod,
            int longPeriod,
            Member member
    ) {
        this.name = name;
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
        this.member = member;
    }

    public Strategy(Long id, String name, int shortPeriod, int longPeriod) {
        this.id = id;
        this.name = name;
        this.shortPeriod = shortPeriod;
        this.longPeriod = longPeriod;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getShortPeriod() {
        return shortPeriod;
    }

    public int getLongPeriod() {
        return longPeriod;
    }
    @JsonIgnore
    public Member getMember() {
        return member;
    }

    public void changeName(String name) {
        this.name = name;
    }
}
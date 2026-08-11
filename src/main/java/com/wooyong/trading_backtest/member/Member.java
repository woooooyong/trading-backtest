package com.wooyong.trading_backtest.member;

import jakarta.persistence.*;


@Entity
@Table(name = "members")

public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

    protected Member(){

    }
    public Member(String email, String password){
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

package com.petmily.domain.builder;

import com.petmily.domain.Member;
import lombok.Getter;

@Getter
public class MemberBuilder {

    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;

    public MemberBuilder(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public MemberBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MemberBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public MemberBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Member getMember() {
        return new Member(this);
    }
}

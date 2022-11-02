package com.petmily;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.Member;
import com.petmily.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class InitService {

    private final MemberService memberService;

    @PostConstruct
    public void init() {
        Member admin = new MemberBuilder("ADMIN", "123")
                .setName("관리자")
                .build();

        memberService.join(admin);
    }
}

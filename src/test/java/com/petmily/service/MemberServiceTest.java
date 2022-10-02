package com.petmily.service;

import com.petmily.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("로그인 아이디가 같은 사용자는 중복 가입 불가 처리함")
    void join() {
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setLoginId("aaa");
        member1.setPassword("123");
        member2.setLoginId("bbb");
        member2.setPassword("456");

        Member duplicatedMember = new Member();
        duplicatedMember.setLoginId("aaa");

        memberService.join(member1);

        Long joinSuccess = memberService.join(member2);

        Assertions.assertThat(joinSuccess).isEqualTo(member2.getId());
        Assertions.assertThatThrownBy(() -> memberService.join(duplicatedMember))
                .isInstanceOf(IllegalStateException.class);
    }

}
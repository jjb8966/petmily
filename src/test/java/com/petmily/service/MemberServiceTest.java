package com.petmily.service;

import com.petmily.domain.Member;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.dto.member.ChangeMemberDto;
import com.petmily.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void crud() {
        Member member = new MemberBuilder("abc", "abc")
                .setName("aaa")
                .setPhone("123")
                .getMember();

        Long joinId = memberService.join(member);
        Member findMember = memberService.findMember(joinId).get();

        assertThat(findMember).isEqualTo(member);

        ChangeMemberDto changeMemberDto = new ChangeMemberDto();
        changeMemberDto.setName("bbb");

        memberService.changeMember(joinId, changeMemberDto);
        Member findMember2 = memberService.findMember(joinId).get();

        assertThat(findMember2.getName()).isEqualTo("bbb");
        assertThatThrownBy(() -> memberService.changeMember(100L, changeMemberDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 회원입니다.");
//        assertThat(findMember2.getLoginId()).isEqualTo("abc");
//        assertThat(findMember2.getPhone()).isEqualTo("123");

        memberService.withdrawMember(member.getId());
        List<Member> allMember = memberService.findAllMember();
        System.out.println("allMember = " + allMember);

        assertThat(allMember.size()).isEqualTo(0);
    }
}
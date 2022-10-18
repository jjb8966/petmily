package com.petmily.repository;

import com.petmily.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryImplTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Commit
    void saveMember() {
        Member member = new Member();
        member.setName("memberA");

        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        assertThat(findMember.getId()).isEqualTo(savedId);
        assertThat(findMember).isEqualTo(member);       // 같은 영속성 컨텍스트 내에서 존재하므로 true
    }

}
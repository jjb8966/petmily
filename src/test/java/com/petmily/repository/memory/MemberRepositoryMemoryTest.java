package com.petmily.repository.memory;

import com.petmily.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryMemoryTest {

    @Autowired
    MemberRepositoryMemory memberRepository;

    @Test
    void save_and_find() {
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setName("joo");
        member1.setGender('M');
        member2.setName("kim");
        member2.setGender('F');

        Long saveId1 = memberRepository.save(member1);
        Long saveId2 = memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(saveId1).get();

        assertThat(findMember1.getName()).isEqualTo("joo");
        assertThat(findMember1.getGender()).isEqualTo('M');

        Member findKim = memberRepository.findByName("kim").get();

        assertThat(findKim.getName()).isEqualTo("kim");
    }

    @Test
    void findAll() {
        Member member1 = new Member();
        Member member2 = new Member();
        member1.setName("joo");
        member2.setName("kim");

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findAllMember = memberRepository.findAll();

        assertThat(findAllMember.containsAll(Arrays.asList(member1, member2))).isTrue();
    }
}
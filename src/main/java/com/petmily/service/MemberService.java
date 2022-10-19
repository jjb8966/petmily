package com.petmily.service;

import com.petmily.domain.Member;
import com.petmily.domain.dto.member.ChangeMemberDto;
import com.petmily.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    public Long join(Member member) {
        memberRepository.save(member);

        return member.getId();
    }

    // 회원 조회
    public Optional<Member> findMember(Long id) {
        return memberRepository.findById(id);
    }

    // 전체 회원 조회
    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    // 회원 정보 변경
    public Long changeMember(Long id, ChangeMemberDto memberDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        member.change(memberDto);

        return member.getId();
    }

    // 회원 탈퇴
    public void withdrawMember(Long id) {
        memberRepository.deleteById(id);
    }
}

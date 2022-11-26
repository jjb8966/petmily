package com.petmily.service;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.LoginForm;
import com.petmily.domain.dto.member.MemberJoinForm;
import com.petmily.domain.dto.member.ModifyMemberForm;
import com.petmily.exception.DuplicateLoginIdException;
import com.petmily.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MessageSource ms;

    // 회원 가입
    @Transactional
    public Long join(MemberJoinForm form) {
        Member member = new MemberBuilder(form.getLoginId(), form.getPassword())
                .setName(form.getName())
                .setBirth(form.getBirth())
                .setEmail(form.getEmail())
                .setPhoneNumber(form.getPhoneNumber())
                .build();

        duplicateCheck(member.getLoginId());
        memberRepository.save(member);

        return member.getId();
    }

    // 로그인
    public Optional<Member> login(LoginForm loginForm) {
        return memberRepository.findByLoginId(loginForm.getLoginId())
                .filter(member -> member.getPassword().equals(loginForm.getPassword()))
                .stream()
                .findAny();
    }

    private void duplicateCheck(String loginId) {
        List<Member> allMember = findAll();

        allMember.stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findAny()
                .ifPresent(m -> {
                    throw new DuplicateLoginIdException(getMessage("exception.duplicate.loginId"));
                });
    }

    // 회원 조회
    public Optional<Member> findOne(Long id) {
        return memberRepository.findById(id);
    }

    // 전체 회원 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // 회원 정보 변경
    @Transactional
    public Long modify(Long id, ModifyMemberForm form) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.member.null")));

        member.changeInfo(form);

        return member.getId();
    }

    // 회원 탈퇴
    @Transactional
    public void withdrawMember(Long id) {
        memberRepository.deleteById(id);
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}

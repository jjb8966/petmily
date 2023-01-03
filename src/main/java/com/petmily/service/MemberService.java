package com.petmily.service;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.LoginForm;
import com.petmily.domain.dto.member.MemberJoinForm;
import com.petmily.domain.dto.member.ModifyMemberForm;
import com.petmily.domain.dto.member.WithdrawMemberForm;
import com.petmily.exception.DuplicateLoginIdException;
import com.petmily.exception.PasswordIncorrectException;
import com.petmily.exception.PasswordMismatchException;
import com.petmily.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final MessageSource ms;

    // 회원 가입
    @Transactional
    public Long join(MemberJoinForm form) {
        passwordMismatchCheck(form.getPassword(), form.getPasswordCheck());

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

    private void passwordMismatchCheck(String password, String passwordCheck) {
        if (hasText(password, passwordCheck) && !matchPasswordCheck(password, passwordCheck)) {
            throw new PasswordMismatchException(getMessage("exception.password.mismatch"));
        }
    }

    private boolean hasText(String password, String passwordCheck) {
        return StringUtils.hasText(password) && StringUtils.hasText(passwordCheck);
    }

    private boolean matchPasswordCheck(String password, String passwordCheck) {
        return password.equals(passwordCheck);
    }

    private void duplicateCheck(String loginId) {
        List<Member> allMember = findAll();

        log.info("error message = {}", getMessage("exception.duplicate.loginId"));

        allMember.stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findAny()
                .ifPresent(m -> {
                    throw new DuplicateLoginIdException(getMessage("exception.duplicate.loginId"));
                });
    }

    // 로그인
    public Optional<Member> login(LoginForm loginForm) {
        return memberRepository.findByLoginId(loginForm.getLoginId())
                .filter(member -> member.getPassword().equals(loginForm.getPassword()))
                .stream()
                .findAny();
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
    public void withdrawMember(Long memberId, String originPassword, WithdrawMemberForm form) {
        passwordMismatchCheck(form.getPassword(), form.getPasswordCheck());
        passwordIncorrectCheck(form.getPassword(), form.getPasswordCheck(), originPassword);

        memberRepository.deleteById(memberId);
    }

    @Transactional
    public void withdrawMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    private void passwordIncorrectCheck(String password, String passwordCheck, String originPassword) {
        if (hasText(password, passwordCheck) && matchPasswordCheck(password, passwordCheck) && !correctPassword(password, originPassword)) {
            throw new PasswordIncorrectException(getMessage("exception.password.incorrect"));
        }
    }

    private boolean correctPassword(String password, String loginMemberPassword) {
        return password.equals(loginMemberPassword);
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}

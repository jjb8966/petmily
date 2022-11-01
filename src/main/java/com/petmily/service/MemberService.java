package com.petmily.service;

import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.ChangeMemberDto;
import com.petmily.exception.DuplicateLoginIdException;
import com.petmily.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationService applicationService;
    private final ReplyService replyService;
    private final BoardService boardService;


    // 회원 가입
    public Long join(Member member) {
        duplicateCheck(member);
        memberRepository.save(member);

        return member.getId();
    }

    private void duplicateCheck(Member member) {
        List<Member> allMember = findAll();
        String loginId = member.getLoginId();

        allMember.stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findAny()
                .ifPresent(m -> {
                    throw new DuplicateLoginIdException("중복된 아이디입니다.");
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
    public Long changeMemberInfo(Long id, ChangeMemberDto memberDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        member.changeInfo(memberDto);

        return member.getId();
    }

    // 회원 탈퇴
    public void withdrawMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        deleteApplicationsAboutMember(id);
        deleteRepliesAboutMember(id);
        deleteBoardsAboutMember(id);

        memberRepository.deleteById(id);
    }

    private void deleteApplicationsAboutMember(Long memberId) {
        applicationService.findAll().stream()
                .filter(application -> application.getMember().getId().equals(memberId))
                .forEach(application -> applicationService.deleteApplication(application.getId()));
    }

    private void deleteRepliesAboutMember(Long memberId) {
        replyService.findAll().stream()
                .filter(reply -> reply.getMember().getId().equals(memberId))
                .forEach(reply -> replyService.deleteReply(reply.getId()));
    }

    private void deleteBoardsAboutMember(Long memberId) {
        boardService.findAll().stream()
                .filter(board -> board.getMember().getId().equals(memberId))
                .forEach(board -> boardService.deleteBoard(board.getId()));
    }
}

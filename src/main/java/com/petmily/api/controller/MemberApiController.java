package com.petmily.api.controller;

import com.petmily.api.ApiResult;
import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.MemberDetailForm;
import com.petmily.domain.dto.member.MemberListForm;
import com.petmily.domain.dto.member.ModifyMemberApiForm;
import com.petmily.domain.dto_converter.MemberDtoConverter;
import com.petmily.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MemberApiController {

    private final MemberService memberService;
    private final MemberDtoConverter memberDtoConverter;
    private final MessageSource ms;

    @GetMapping("/members")
    public ApiResult getList() {
        List<Member> allMembers = memberService.findAll();

        List<MemberListForm> memberListForms = allMembers.stream()
                .map(member -> memberDtoConverter.entityToDto(member, MemberListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert"))))
                .collect(Collectors.toList());

        ApiResult<List<MemberListForm>> result = new ApiResult<>(memberListForms);
        result.setCount(memberListForms.size());

        return result;
    }

    @GetMapping("/members/{memberId}")
    public MemberDetailForm memberDetail(@PathVariable Long memberId) {
        Member member = getMember(memberId);

        return memberDtoConverter.entityToDto(member, MemberDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));
    }

    private Member getMember(Long memberId) {
        return memberService.findOne(memberId).orElseThrow(() -> new IllegalArgumentException(getMessage("exception.member.null")));
    }

    @DeleteMapping("/members/{memberId}")
    public Map<String, String> withdrawMember(@PathVariable Long memberId) {
        memberService.withdrawMember(memberId);

        return Map.of("message", "회원이 탈퇴되었습니다.");
    }

    @PatchMapping("/members/{memberId}")
    public Map<String, String> modifyMember(@PathVariable Long memberId,
                                            @RequestBody ModifyMemberApiForm form) throws ParseException {

        log.info("modify member form = {}", form);

        memberService.modify(memberId, form);

        return Map.of("message", "회원이 정보가 변경되었습니다.");
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}

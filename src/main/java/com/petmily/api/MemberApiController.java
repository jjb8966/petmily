package com.petmily.api;

import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.MemberListForm;
import com.petmily.domain.dto_converter.MemberDtoConverter;
import com.petmily.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/members/{memberId}")
    public Map<String, String> deleteMember(@PathVariable Long memberId) {
        memberService.withdrawMember(memberId);

        return Map.of("message", "회원이 탈퇴되었습니다.");
    }

    @Getter
    @Setter
    static class ApiResult<T> {

        private Integer count;
        private T data;

        public ApiResult(T data) {
            this.data = data;
        }
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}

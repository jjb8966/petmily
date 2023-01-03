package com.petmily.domain.dto.member;

import com.petmily.domain.enum_type.MemberGrade;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MemberListForm {

    private Long id;
    private MemberGrade grade;
    private String loginId;
    private String name;
}

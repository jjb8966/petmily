package com.petmily.domain.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@Setter
public class MemberDetailForm {

    private String loginId;
    private String password;
    private String name;
    private LocalDate birth;
    private String grade;
    private String phoneNumber;
    private String email;
}

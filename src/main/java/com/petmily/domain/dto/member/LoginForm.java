package com.petmily.domain.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Getter @Setter
public class LoginForm {

    @NotBlank(message = "아이디를 입력하세요.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
}

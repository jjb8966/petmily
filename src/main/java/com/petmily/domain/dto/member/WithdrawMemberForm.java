package com.petmily.domain.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class WithdrawMemberForm {

    @NotBlank
    private String password;

    @NotBlank
    private String passwordCheck;
}

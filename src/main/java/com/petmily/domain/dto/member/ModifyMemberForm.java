package com.petmily.domain.dto.member;

import com.petmily.domain.embeded_type.Email;
import com.petmily.domain.embeded_type.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ModifyMemberForm {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotNull
    private Email email;

    @NotNull
    private PhoneNumber phoneNumber;
}

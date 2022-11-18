package com.petmily.domain.dto.member;

import com.petmily.domain.core.embeded_type.PhoneNumber;
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

    @NotBlank
    private String email;

    @NotNull
    private PhoneNumber phoneNumber;
}

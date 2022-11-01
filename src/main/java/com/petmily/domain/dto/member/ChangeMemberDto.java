package com.petmily.domain.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeMemberDto {

    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;
}

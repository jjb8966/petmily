package com.petmily.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Member {

    private Long id;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private char gender;
    private String phoneNumber;
    private String grade;
    private LocalDateTime birth;
}

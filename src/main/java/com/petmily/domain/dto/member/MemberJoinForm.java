package com.petmily.domain.dto.member;

import com.petmily.domain.embeded_type.Email;
import com.petmily.domain.embeded_type.PhoneNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ToString
@Getter
@Setter
public class MemberJoinForm {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordCheck;

    @NotBlank
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    @NotNull
    private Email email;

    @NotNull
    private PhoneNumber phoneNumber;
}

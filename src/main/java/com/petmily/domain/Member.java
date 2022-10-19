package com.petmily.domain;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.dto.member.ChangeMemberDto;
import com.petmily.domain.enum_type.MemberGrade;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Board> boards;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Reply> replies;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Application> applications;

    @Enumerated(EnumType.STRING)
    private MemberGrade grade;

    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;

    public Member(MemberBuilder memberBuilder) {
        this.loginId = memberBuilder.getLoginId();
        this.password = memberBuilder.getPassword();
        this.name = memberBuilder.getName();
        this.email = memberBuilder.getEmail();
        this.phone = memberBuilder.getPhone();
    }

    public void change(ChangeMemberDto memberDto) {
        this.loginId = memberDto.getLoginId();
        this.password = memberDto.getPassword();
        this.name = memberDto.getName();
        this.email = memberDto.getEmail();
        this.phone = memberDto.getPhone();
    }
}
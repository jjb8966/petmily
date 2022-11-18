package com.petmily.domain.core;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.embeded_type.Email;
import com.petmily.domain.core.embeded_type.PhoneNumber;
import com.petmily.domain.core.enum_type.MemberGrade;
import com.petmily.domain.dto.member.ModifyMemberForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@ToString(of = {"loginId", "password", "name", "birth", "email", "phone", "grade"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

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

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Email email;

    private String loginId;
    private String password;
    private String name;
    private LocalDate birth;

    public Member(MemberBuilder builder) {
        this.boards = builder.getBoards();
        this.replies = builder.getReplies();
        this.applications = builder.getApplies();
        this.grade = builder.getGrade();
        this.loginId = builder.getLoginId();
        this.password = builder.getPassword();
        this.name = builder.getName();
        this.birth = builder.getBirth();
        this.email = builder.getEmail();
        this.phoneNumber = builder.getPhoneNumber();
    }

    public void changeInfo(ModifyMemberForm memberDto) {
        this.password = memberDto.getPassword();
        this.name = memberDto.getName();
        this.email = memberDto.getEmail();
        this.phoneNumber = memberDto.getPhoneNumber();
    }

    public void deleteBoard(Board board) {
        boards.remove(board);
    }
}
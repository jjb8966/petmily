package com.petmily.domain.builder;

import com.petmily.domain.Application;
import com.petmily.domain.Board;
import com.petmily.domain.Member;
import com.petmily.domain.Reply;
import com.petmily.domain.enum_type.MemberGrade;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MemberBuilder {

    private List<Board> boards = new ArrayList<>();
    private List<Reply> replies = new ArrayList<>();
    private List<Application> applications = new ArrayList<>();
    private MemberGrade grade;
    private String loginId;
    private String password;
    private String name;
    private String email;
    private String phone;

    public MemberBuilder(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public Member build() {
        return new Member(this);
    }

    public MemberBuilder setBoards(List<Board> boards) {
        this.boards = boards;
        return this;
    }

    public MemberBuilder setReplies(List<Reply> replies) {
        this.replies = replies;
        return this;
    }

    public MemberBuilder setApplication(List<Application> applications) {
        this.applications = applications;
        return this;
    }

    public MemberBuilder setMemberGrade(MemberGrade grade) {
        this.grade = grade;
        return this;
    }

    public MemberBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public MemberBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public MemberBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }
}

package com.petmily.domain;

import com.petmily.domain.enum_type.MemberGrade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
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

}
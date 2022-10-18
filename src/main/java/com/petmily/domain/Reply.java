package com.petmily.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Reply {

    @Id
    @GeneratedValue
    @Column(name = "reply_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String content;
    private LocalDateTime writeTime;
}

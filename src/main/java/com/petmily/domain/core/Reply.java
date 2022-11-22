package com.petmily.domain.core;

import com.petmily.domain.builder.ReplyBuilder;
import com.petmily.domain.dto.reply.ChangeReplyForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply extends BaseEntity {

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

    public Reply(ReplyBuilder builder) {
        this.member = builder.getMember();
        this.board = builder.getBoard();
        this.content = builder.getContent();
    }

    public void changeInfo(ChangeReplyForm replyDto) {
        this.content = replyDto.getContent();
    }

    @Override
    public String toString() {
        return "Reply{" +
                "id=" + id +
                ", member=" + member +
                ", board.getId()=" + board.getId() +
                ", content='" + content + '\'' +
                '}';
    }

}

package com.petmily.domain.core.board;

import com.petmily.domain.builder.board.BoardBuilder;
import com.petmily.domain.core.BaseEntity;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.Reply;
import com.petmily.domain.dto.board.ModifyBoardApiForm;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.enum_type.BoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@ToString
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    protected Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Reply> replies;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Picture> pictures;

    @Enumerated(EnumType.STRING)
    protected BoardType boardType;

    protected String title;
    protected String content;
    protected Boolean shownAll;

    public Board(BoardBuilder builder) {
        this.member = builder.getMember();
        this.replies = builder.getReplies();
        this.pictures = builder.getPictures();
        this.boardType = builder.getBoardType();
        this.title = builder.getTitle();
        this.content = builder.getContent();
        this.shownAll = builder.getShownAll();
    }

    public void changeInfo(ModifyBoardForm from) {
        this.title = from.getTitle();
        this.content = from.getContent();
        this.shownAll = from.getShownAll();
    }

    public void changeInfo(ModifyBoardApiForm apiForm) {
        this.boardType = BoardType.valueOf(apiForm.getBoardType());
        this.title = apiForm.getTitle();
        this.content = apiForm.getContent();
        this.shownAll = apiForm.getShownAll();
    }
}

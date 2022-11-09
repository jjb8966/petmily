package com.petmily.domain.core;

import com.petmily.domain.builder.BoardBuilder;
import com.petmily.domain.core.enum_type.BoardType;
import com.petmily.domain.dto.board.ModifyBoardForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Reply> replies;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Picture> pictures;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private String title;
    private String content;
    private Boolean shownAll;

    public Board(BoardBuilder builder) {
        this.member = builder.getMember();
        this.replies = builder.getReplies();
        this.pictures = builder.getPictures();
        this.boardType = builder.getBoardType();
        this.title = builder.getTitle();
        this.content = builder.getContent();
        this.shownAll = builder.isShownAll();
    }

    public void changeInfo(ModifyBoardForm from) {
        this.title = from.getTitle();
        this.content = from.getContent();
        this.shownAll = from.getShownAll();
    }

    public void clearPicture() {
        for (Picture picture : pictures) {
            picture.deleteBoard();
        }

        this.pictures = new ArrayList<>();
    }

}

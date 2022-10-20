package com.petmily.domain;

import com.petmily.domain.builder.BoardBuilder;
import com.petmily.domain.dto.board.ChangeBoardDto;
import com.petmily.domain.embedded_type.Picture;
import com.petmily.domain.enum_type.BoardType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Reply> replies;

    @ElementCollection
    private List<Picture> pictures;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private String title;
    private String content;
    private boolean shownAll;
    private LocalDateTime writeTime;

    public Board(BoardBuilder builder) {
        this.member = builder.getMember();
        this.replies = builder.getReplies();
        this.pictures = builder.getPictures();
        this.boardType = builder.getBoardType();
        this.title = builder.getTitle();
        this.content = builder.getContent();
        this.shownAll = builder.isShownAll();
        this.writeTime = builder.getWriteTime();
    }

    public void changeInfo(ChangeBoardDto boardDto) {
        this.pictures = boardDto.getPictures();
        this.title = boardDto.getTitle();
        this.content = boardDto.getContent();
        this.shownAll = boardDto.isShownAll();
    }
}

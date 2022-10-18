package com.petmily.domain;

import com.petmily.domain.embedded_type.Picture;
import com.petmily.domain.enum_type.BoardType;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
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
}

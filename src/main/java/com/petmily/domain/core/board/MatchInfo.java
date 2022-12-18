package com.petmily.domain.core.board;

import com.petmily.domain.builder.board.MatchInfoBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MatchInfo {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private FindWatchBoard targetBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private FindWatchBoard matchedBoard;

    public MatchInfo(MatchInfoBuilder builder) {
        targetBoard = builder.getTargetBoard();
        matchedBoard = builder.getMatchedBoard();
    }

}

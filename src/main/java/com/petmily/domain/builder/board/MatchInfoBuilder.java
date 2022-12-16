package com.petmily.domain.builder.board;

import com.petmily.domain.core.board.FindWatchBoard;
import com.petmily.domain.core.board.MatchInfo;
import lombok.Getter;

@Getter
public class MatchInfoBuilder {

    private FindWatchBoard targetBoard;
    private FindWatchBoard matchedBoard;

    public MatchInfoBuilder(FindWatchBoard targetBoard, FindWatchBoard matchedBoard) {
        this.targetBoard = targetBoard;
        this.matchedBoard = matchedBoard;
    }

    public MatchInfo build() {
        MatchInfo matchInfo = new MatchInfo(this);

        targetBoard.addMatchedBoardInfo(matchInfo);
        matchedBoard.addTargetBoardInfo(matchInfo);

        return matchInfo;
    }
}

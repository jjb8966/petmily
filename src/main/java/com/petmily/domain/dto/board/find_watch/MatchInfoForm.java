package com.petmily.domain.dto.board.find_watch;

import com.petmily.domain.enum_type.BoardType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MatchInfoForm {

    private Long myBoardId;
    private BoardType boardType;
    private String myBoardTitle;
    private List<FindWatchBoardListForm> boardListForm;
}

package com.petmily.repository;

import com.petmily.domain.core.board.Board;
import com.petmily.domain.core.board.FindWatchBoard;
import com.petmily.domain.dto.board.find_watch.SearchCondition;
import com.petmily.domain.enum_type.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface BoardRepositoryCustom {

    Page<Board> findAllByBoardType(BoardType boardType, SearchCondition searchCondition, Pageable pageable);

    List<Long> matchWithFindWatchBoard(FindWatchBoard board);

    Long updateBoardStatusMatch(ArrayList<Long> needUpdateIds);
}

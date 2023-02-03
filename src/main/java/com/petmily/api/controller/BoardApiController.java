package com.petmily.api.controller;

import com.petmily.api.ApiResult;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.dto.board.BoardDetailForm;
import com.petmily.domain.dto.board.BoardListForm;
import com.petmily.domain.dto.board.ModifyBoardApiForm;
import com.petmily.domain.dto_converter.BoardDtoConverter;
import com.petmily.service.BoardService;
import com.petmily.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BoardApiController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final BoardDtoConverter boardDtoConverter;
    private final MessageSource ms;

    @GetMapping("/boards")
    public ApiResult getList() {
        List<Board> allBoards = boardService.findAll();

        List<BoardListForm> boardListForms = allBoards.stream()
                .map(board -> boardDtoConverter.entityToDto(board, BoardListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert"))))
                .collect(Collectors.toList());

        Collections.sort(boardListForms, Comparator.comparing(BoardListForm::getBoardType));

        ApiResult<List<BoardListForm>> result = new ApiResult<>(boardListForms);
        result.setCount(boardListForms.size());

        return result;
    }

    @GetMapping("/boards/{boardId}")
    public BoardDetailForm boardDetail(@PathVariable Long boardId) {
        Board board = getBoard(boardId);

        return boardDtoConverter.entityToDto(board, BoardDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));
    }

    private Board getBoard(Long boardId) {
        return boardService.findOne(boardId).orElseThrow(() -> new IllegalArgumentException(getMessage("exception.board.null")));
    }

    @DeleteMapping("/boards/{boardId}")
    public Map<String, String> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);

        return Map.of("message", "게시글이 삭제되었습니다.");
    }

    @DeleteMapping("/boards/replies/{replyId}")
    public Map<String, String> deleteReply(@PathVariable Long replyId) {
        replyService.deleteReply(replyId);

        return Map.of("message", "댓글이 삭제되었습니다.");
    }

    @PatchMapping("/boards/{boardId}")
    public Map<String, String> modifyBoard(@PathVariable Long boardId,
                                           @RequestBody ModifyBoardApiForm form) {

        log.info("modify board form = {}", form);

        boardService.modifyBoardInfo(boardId, form);

        return Map.of("message", "게시글 정보가 변경되었습니다.");
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}

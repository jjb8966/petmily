package com.petmily.domain.dto_converter;

import com.petmily.domain.core.BaseEntity;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.core.board.FindBoard;
import com.petmily.domain.core.board.WatchBoard;
import com.petmily.domain.dto.board.BoardDetailForm;
import com.petmily.domain.dto.board.BoardListForm;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.dto.board.find_watch.FindWatchBoardListForm;
import com.petmily.domain.dto.picutre.BoardPictureForm;
import com.petmily.domain.dto.reply.ReplyDetailForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BoardDtoConverter implements EntityDtoConverter {

    @Override
    public <T> Optional<T> entityToDto(BaseEntity entity, Class<T> dtoType) {
        Board board = (Board) entity;
        T dto = null;

        if (dtoType.isAssignableFrom(BoardListForm.class)) {
            log.info("Board -> BoardListForm");
            dto = (T) convertToBoardListForm(board);
        }

        if (dtoType.isAssignableFrom(BoardDetailForm.class)) {
            log.info("Board -> BoardDetailForm");
            dto = (T) convertToBoardDetailForm(board);
        }

        if (dtoType.isAssignableFrom(ModifyBoardForm.class)) {
            log.info("Board -> ModifyBoardForm");
            dto = (T) convertToModifyBoardForm(board);
        }

        if (dtoType.isAssignableFrom(FindWatchBoardListForm.class)) {
            log.info("Board -> FindWatchBoardDetailForm");
            dto = (T) convertToFindWatchBoardListForm(board);
        }

        return Optional.ofNullable(dto);
    }

    private FindWatchBoardListForm convertToFindWatchBoardListForm(Board board) {
        FindWatchBoardListForm findWatchBoardListForm = new FindWatchBoardListForm();

        findWatchBoardListForm.setId(board.getId());
        findWatchBoardListForm.setMemberId(board.getMember().getId());
        findWatchBoardListForm.setWriterName(board.getMember().getName());
        findWatchBoardListForm.setTitle(board.getTitle());
        findWatchBoardListForm.setCreatedDate(board.getCreatedDate());
        findWatchBoardListForm.setShownAll(board.getShownAll());
        findWatchBoardListForm.setBoardType(board.getBoardType());

        if (board instanceof FindBoard) {
            FindBoard findBoard = (FindBoard) board;
            findWatchBoardListForm.setLostOrWatchTime(findBoard.getLostTime());
            findWatchBoardListForm.setSpecies(findBoard.getSpecies());

            if (!findBoard.getPictures().isEmpty()) {
                findWatchBoardListForm.setThumbnail(findBoard.getPictures().get(0));
            }
        }

        if (board instanceof WatchBoard) {
            WatchBoard watchBoard = (WatchBoard) board;
            findWatchBoardListForm.setLostOrWatchTime(watchBoard.getWatchTime());
            findWatchBoardListForm.setSpecies(watchBoard.getSpecies());

            if (!watchBoard.getPictures().isEmpty()) {
                findWatchBoardListForm.setThumbnail(watchBoard.getPictures().get(0));
            }
        }

        return findWatchBoardListForm;
    }

    private BoardListForm convertToBoardListForm(Board board) {
        BoardListForm boardForm = new BoardListForm();

        boardForm.setId(board.getId());
        boardForm.setMemberId(board.getMember().getId());
        boardForm.setWriterName(board.getMember().getName());
        boardForm.setTitle(board.getTitle());
        boardForm.setCreatedDate(board.getCreatedDate());
        boardForm.setShownAll(board.getShownAll());
        boardForm.setBoardType(board.getBoardType());

        return boardForm;
    }

    private BoardDetailForm convertToBoardDetailForm(Board board) {
        BoardDetailForm boardForm = new BoardDetailForm();

        boardForm.setId(board.getId());
        boardForm.setCreatedDate(board.getCreatedDate());
        boardForm.setLastModifiedDate(board.getLastModifiedDate());
        boardForm.setMember(board.getMember());
        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        boardForm.setShownAll(board.getShownAll());

        setReplyForms(board.getReplies(), boardForm);
        setPictureForms(board.getPictures(), boardForm);

        return boardForm;
    }

    private void setReplyForms(List<Reply> replies, BoardDetailForm boardForm) {
        List<ReplyDetailForm> replyForms = replies.stream()
                .map(reply -> {
                    ReplyDetailForm form = new ReplyDetailForm();

                    form.setId(reply.getId());
                    form.setMemberId(reply.getMember().getId());
                    form.setLastModifiedDate(reply.getLastModifiedDate());
                    form.setWriterName(reply.getMember().getName());
                    form.setContent(reply.getContent());

                    return form;
                })
                .collect(Collectors.toList());

        boardForm.setReplies(replyForms);
    }

    private void setPictureForms(List<Picture> pictures, BoardDetailForm boardForm) {
        List<BoardPictureForm> pictureForms = pictures.stream()
                .map(picture -> {
                    BoardPictureForm form = new BoardPictureForm();
                    form.setFileStoreName(picture.getFileStoreName());

                    return form;
                })
                .collect(Collectors.toList());

        boardForm.setPictures(pictureForms);
    }

    private ModifyBoardForm convertToModifyBoardForm(Board board) {
        ModifyBoardForm boardForm = new ModifyBoardForm();

        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        boardForm.setShownAll(board.getShownAll());

        return boardForm;
    }
}

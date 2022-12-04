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
import com.petmily.domain.dto.board.find_watch.FindWatchBoardDetailForm;
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
            log.info("Board -> FindWatchBoardListForm");
            dto = (T) convertToFindWatchBoardListForm(board);
        }

        if (dtoType.isAssignableFrom(FindWatchBoardDetailForm.class)) {
            log.info("Board -> FindWatchBoardDetailForm");
            dto = (T) convertToFindWatchBoardDetailForm(board);
        }

        return Optional.ofNullable(dto);
    }

    private FindWatchBoardDetailForm convertToFindWatchBoardDetailForm(Board board) {
        FindWatchBoardDetailForm boardDetailForm = new FindWatchBoardDetailForm();

        setBasicInformationForDetailForm(board, boardDetailForm);
        setReplyForms(board.getReplies(), boardDetailForm);
        setPictureForms(board.getPictures(), boardDetailForm);

        if (board instanceof FindBoard) {
            setFindBoardDetailForm((FindBoard) board, boardDetailForm);
        }

        if (board instanceof WatchBoard) {
            setWatchBoardDetailForm((WatchBoard) board, boardDetailForm);
        }

        return boardDetailForm;
    }

    private void setFindBoardDetailForm(FindBoard findBoard, FindWatchBoardDetailForm boardDetailForm) {
        boardDetailForm.setLostOrWatchTime(findBoard.getLostTime());
        boardDetailForm.setSpecies(findBoard.getSpecies());
        boardDetailForm.setAnimalName(findBoard.getAnimalName());
        boardDetailForm.setAnimalKind(findBoard.getAnimalKind());
        boardDetailForm.setAnimalAge(findBoard.getAnimalAge());
        boardDetailForm.setAnimalWeight(findBoard.getAnimalWeight());
    }

    private void setWatchBoardDetailForm(WatchBoard watchBoard, FindWatchBoardDetailForm boardDetailForm) {
        boardDetailForm.setLostOrWatchTime(watchBoard.getWatchTime());
        boardDetailForm.setSpecies(watchBoard.getSpecies());
    }

    private FindWatchBoardListForm convertToFindWatchBoardListForm(Board board) {
        FindWatchBoardListForm boardListForm = new FindWatchBoardListForm();

        setBasicInformationForListForm(board, boardListForm);

        if (board instanceof FindBoard) {
            setFindBoardListForm((FindBoard) board, boardListForm);
        }

        if (board instanceof WatchBoard) {
            setWatchBoardListForm((WatchBoard) board, boardListForm);
        }

        return boardListForm;
    }

    private static void setWatchBoardListForm(WatchBoard watchBoard, FindWatchBoardListForm findWatchBoardListForm) {
        findWatchBoardListForm.setLostOrWatchTime(watchBoard.getWatchTime());
        findWatchBoardListForm.setSpecies(watchBoard.getSpecies());

        if (!watchBoard.getPictures().isEmpty()) {
            findWatchBoardListForm.setThumbnail(watchBoard.getPictures().get(0));
        }
    }

    private static void setFindBoardListForm(FindBoard findBoard, FindWatchBoardListForm findWatchBoardListForm) {
        findWatchBoardListForm.setLostOrWatchTime(findBoard.getLostTime());
        findWatchBoardListForm.setSpecies(findBoard.getSpecies());

        if (!findBoard.getPictures().isEmpty()) {
            findWatchBoardListForm.setThumbnail(findBoard.getPictures().get(0));
        }
    }

    private BoardListForm convertToBoardListForm(Board board) {
        BoardListForm boardListForm = new BoardListForm();

        setBasicInformationForListForm(board, boardListForm);

        return boardListForm;
    }

    private static void setBasicInformationForListForm(Board board, BoardListForm boardForm) {
        boardForm.setId(board.getId());
        boardForm.setMemberId(board.getMember().getId());
        boardForm.setWriterName(board.getMember().getName());
        boardForm.setTitle(board.getTitle());
        boardForm.setCreatedDate(board.getCreatedDate());
        boardForm.setShownAll(board.getShownAll());
        boardForm.setBoardType(board.getBoardType());
    }

    private BoardDetailForm convertToBoardDetailForm(Board board) {
        BoardDetailForm boardDetailForm = new BoardDetailForm();

        setBasicInformationForDetailForm(board, boardDetailForm);
        setReplyForms(board.getReplies(), boardDetailForm);
        setPictureForms(board.getPictures(), boardDetailForm);

        return boardDetailForm;
    }

    private static void setBasicInformationForDetailForm(Board board, BoardDetailForm boardForm) {
        boardForm.setId(board.getId());
        boardForm.setCreatedDate(board.getCreatedDate());
        boardForm.setLastModifiedDate(board.getLastModifiedDate());
        boardForm.setMember(board.getMember());
        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        boardForm.setShownAll(board.getShownAll());
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
        ModifyBoardForm modifyBoardForm = new ModifyBoardForm();

        setBasicInformationForModifyForm(board, modifyBoardForm);

        if (board instanceof FindBoard) {
            setModifyFindBoardForm((FindBoard) board, modifyBoardForm);
        }

        if (board instanceof WatchBoard) {
            setModifyWatchBoardForm((WatchBoard) board, modifyBoardForm);
        }

        return modifyBoardForm;
    }

    private void setModifyFindBoardForm(FindBoard findBoard, ModifyBoardForm modifyBoardForm) {
        modifyBoardForm.setLostOrWatchTime(findBoard.getLostTime());
        modifyBoardForm.setSpecies(findBoard.getSpecies());
        modifyBoardForm.setAnimalName(findBoard.getAnimalName());
        modifyBoardForm.setAnimalKind(findBoard.getAnimalKind());
        modifyBoardForm.setAnimalAge(findBoard.getAnimalAge());
        modifyBoardForm.setAnimalWeight(findBoard.getAnimalWeight());
    }

    private void setModifyWatchBoardForm(WatchBoard watchBoard, ModifyBoardForm modifyBoardForm) {
        modifyBoardForm.setLostOrWatchTime(watchBoard.getWatchTime());
        modifyBoardForm.setSpecies(watchBoard.getSpecies());
    }

    private static void setBasicInformationForModifyForm(Board board, ModifyBoardForm boardForm) {
        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        boardForm.setShownAll(board.getShownAll());
    }
}

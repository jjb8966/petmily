package com.petmily.domain.dto_converter;

import com.petmily.domain.core.BaseEntity;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.core.board.FindWatchBoard;
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

        if (BoardListForm.class.isAssignableFrom(dtoType)) {
            log.info("Board -> BoardListForm");
            dto = (T) convertToBoardListForm(board);
        }

        if (BoardDetailForm.class.isAssignableFrom(dtoType)) {
            log.info("Board -> BoardDetailForm");
            dto = (T) convertToBoardDetailForm(board);
        }

        if (ModifyBoardForm.class.isAssignableFrom(dtoType)) {
            log.info("Board -> ModifyBoardForm");
            dto = (T) convertToModifyBoardForm(board);
        }

        if (FindWatchBoardListForm.class.isAssignableFrom(dtoType)) {
            log.info("Board -> FindWatchBoardListForm");
            dto = (T) convertToFindWatchBoardListForm(board);
        }

        if (FindWatchBoardDetailForm.class.isAssignableFrom(dtoType)) {
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
        setFindWatchBoardDetailForm((FindWatchBoard) board, boardDetailForm);

        return boardDetailForm;
    }

    private void setFindWatchBoardDetailForm(FindWatchBoard findWatchBoard, FindWatchBoardDetailForm boardDetailForm) {
        boardDetailForm.setLostOrWatchTime(findWatchBoard.getLostOrWatchTime());
        boardDetailForm.setSpecies(findWatchBoard.getSpecies());
        boardDetailForm.setAnimalName(findWatchBoard.getAnimalName());
        boardDetailForm.setAnimalKind(findWatchBoard.getAnimalKind());
        boardDetailForm.setAnimalAge(findWatchBoard.getAnimalAge());
        boardDetailForm.setAnimalWeight(findWatchBoard.getAnimalWeight());
    }

    private FindWatchBoardListForm convertToFindWatchBoardListForm(Board board) {
        FindWatchBoardListForm boardListForm = new FindWatchBoardListForm();

        setBasicInformationForListForm(board, boardListForm);
        setFindWatchBoardListForm((FindWatchBoard) board, boardListForm);

        return boardListForm;
    }

    private static void setFindWatchBoardListForm(FindWatchBoard findWatchBoard, FindWatchBoardListForm findWatchBoardListForm) {
        findWatchBoardListForm.setLostOrWatchTime(findWatchBoard.getLostOrWatchTime());
        findWatchBoardListForm.setSpecies(findWatchBoard.getSpecies());

        if (!findWatchBoard.getPictures().isEmpty()) {
            findWatchBoardListForm.setThumbnail(findWatchBoard.getPictures().get(0));
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

        if (board instanceof FindWatchBoard) {
            setModifyFindWatchBoardForm((FindWatchBoard) board, modifyBoardForm);
        }

        return modifyBoardForm;
    }

    private void setModifyFindWatchBoardForm(FindWatchBoard findWatchBoard, ModifyBoardForm modifyBoardForm) {
        modifyBoardForm.setLostOrWatchTime(findWatchBoard.getLostOrWatchTime());
        modifyBoardForm.setSpecies(findWatchBoard.getSpecies());
        modifyBoardForm.setAnimalName(findWatchBoard.getAnimalName());
        modifyBoardForm.setAnimalKind(findWatchBoard.getAnimalKind());
        modifyBoardForm.setAnimalAge(findWatchBoard.getAnimalAge());
        modifyBoardForm.setAnimalWeight(findWatchBoard.getAnimalWeight());
    }

    private static void setBasicInformationForModifyForm(Board board, ModifyBoardForm boardForm) {
        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        boardForm.setShownAll(board.getShownAll());
    }
}

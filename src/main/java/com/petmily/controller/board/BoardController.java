package com.petmily.controller.board;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.enum_type.BoardType;
import com.petmily.domain.dto.PetmilyPage;
import com.petmily.domain.dto.board.BoardDetailForm;
import com.petmily.domain.dto.board.BoardListForm;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.dto.board.WriteBoardForm;
import com.petmily.domain.dto.picutre.BoardPictureForm;
import com.petmily.domain.dto.reply.ReplyDetailForm;
import com.petmily.domain.dto.reply.WriteReplyForm;
import com.petmily.repository.BoardRepository;
import com.petmily.service.BoardService;
import com.petmily.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final ReplyService replyService;

    @ResponseBody
    @GetMapping("/image/{fileStoreName}")
    public Resource getImage(@PathVariable String fileStoreName) throws MalformedURLException {
        log.info("fileStoreName = {}", fileStoreName);
        String fullPath = "file:" + "/Users/joojongbum/store_image/" + fileStoreName;
        log.info("full path = {}", fullPath);

        return new UrlResource(fullPath);
    }

    @GetMapping("/{boardType}/list")
    public String list(@PathVariable BoardType boardType,
                       @PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {

        Page<Board> allBoards = boardRepository.findAllByBoardType(boardType, pageable);

        log.info("total element = {}", allBoards.getTotalElements());
        log.info("total pages = {}", allBoards.getTotalPages());

        PetmilyPage<BoardListForm> boardPage = changeToBoardPage(allBoards);

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("boardPage", boardPage);

        return "/view/board/board_list";
    }

    private PetmilyPage<BoardListForm> changeToBoardPage(Page<Board> allBoards) {
        Page<BoardListForm> allBoardsForm = allBoards.map(board -> {
            BoardListForm boardForm = new BoardListForm();

            boardForm.setId(board.getId());
            boardForm.setMemberId(board.getMember().getId());
            boardForm.setWriterName(board.getMember().getName());
            boardForm.setTitle(board.getTitle());
            boardForm.setCreatedDate(board.getCreatedDate());
            boardForm.setShownAll(board.getShownAll());

            return boardForm;
        });

        return new PetmilyPage<>(allBoardsForm);
    }

    @GetMapping("/{boardType}/detail/{boardId}")
    public String detailForm(@PathVariable BoardType boardType,
                             @PathVariable Long boardId,
                             Model model) {

        log.info("board type = {}, id = {}", boardType, boardId);

        Board board = boardService.findOne(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        BoardDetailForm boardForm = changeToBoardDetailForm(board);

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("board", boardForm);
        model.addAttribute("writeReplyForm", new WriteReplyForm());

        return "/view/board/detail_form";
    }

    private BoardDetailForm changeToBoardDetailForm(Board board) {
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

    private static void setReplyForms(List<Reply> replies, BoardDetailForm boardForm) {
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

    @GetMapping("/{boardType}/auth/write")
    public String writeForm(@PathVariable BoardType boardType, Model model) {
        log.info("board type = {}", boardType);

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("form", new WriteBoardForm());

        return "/view/board/write_form";
    }

    @PostMapping("/{boardType}/auth/write")
    public String write(@PathVariable BoardType boardType,
                        @SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                        @ModelAttribute("form") @Valid WriteBoardForm form,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        log.info("form = {}", form);

        if (bindingResult.hasErrors()) {
            log.info("게시글 작성 실패 {}", bindingResult.getAllErrors());
            return "/view/board/write_form";
        }

        Long boardId = boardService.write(loginMember.getId(), boardType, form);

        redirectAttributes.addAttribute("boardType", boardType.name().toLowerCase());
        redirectAttributes.addAttribute("id", boardId);

        return "redirect:/board/{boardType}/detail/{id}";
    }

    @GetMapping("/{boardType}/auth/modify/{id}")
    public String modifyForm(@PathVariable BoardType boardType,
                             @PathVariable Long id,
                             Model model) {

        log.info("board type = {}, id = {}", boardType, id);

        Board board = boardService.findOne(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        ModifyBoardForm boardForm = changeToModifyForm(board);

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("id", id);
        model.addAttribute("form", boardForm);

        return "/view/board/modify_form";
    }

    private static ModifyBoardForm changeToModifyForm(Board board) {
        ModifyBoardForm boardForm = new ModifyBoardForm();

        boardForm.setTitle(board.getTitle());
        boardForm.setContent(board.getContent());
        boardForm.setShownAll(board.getShownAll());

        return boardForm;
    }

    @PostMapping("/{boardType}/auth/modify/{id}")
    public String modify(@PathVariable BoardType boardType,
                         @PathVariable Long id,
                         @ModelAttribute("form") @Valid ModifyBoardForm form,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {

        log.info("board type = {}, id = {}, form = {}", boardType, id, form);
        log.info("is empty = {}", form.getPictures().get(0).isEmpty());

        if (bindingResult.hasErrors()) {
            log.info("게시글 수정 실패 {}", bindingResult.getAllErrors());
            return "/view/board/modify_form";
        }

        boardService.modifyBoardInfo(id, form);

        Board board = boardService.findOne(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        log.info("게시글 수정 완료 {}", board);

        redirectAttributes.addAttribute("boardType", boardType.name().toLowerCase());
        redirectAttributes.addAttribute("id", id);

        return "redirect:/board/{boardType}/detail/{id}";
    }

    @GetMapping("/{boardType}/auth/delete/{id}")
    public String delete(@PathVariable BoardType boardType,
                         @PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        boardService.deleteBoard(id);

        log.info("게시글 삭제 완료");

        redirectAttributes.addAttribute("boardType", boardType.name().toLowerCase());
        redirectAttributes.addAttribute("id", id);

        return "redirect:/board/{boardType}/list";
    }

    @PostMapping("/board/{boardType}/auth/reply/{id}")
    public String reply(@PathVariable BoardType boardType,
                        @PathVariable Long boardId,
                        @SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                        @ModelAttribute WriteReplyForm form,
                        Model model) {

        log.info("board type = {}, id = {}", boardType, boardId);

        Board board = boardService.findOne(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        BoardDetailForm boardForm = changeToBoardDetailForm(board);
        Long replyId = replyService.reply(loginMember.getId(), boardId, form);

        Reply reply = replyService.findOne(replyId);

        log.info("댓글 작성 완료 {}", reply);

        List<Reply> replies = replyService.findAll();

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("board", boardForm);
        model.addAttribute("replies", replies);

        return "redirect:/board/{boardType}/detail/{id}";
    }

    @PostMapping("/{boardType}/{boardId}/auth/reply/write")
    public String writeReply(@PathVariable BoardType boardType,
                             @PathVariable Long boardId,
                             @SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                             @ModelAttribute @Valid WriteReplyForm form,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        log.info("reply content = {}", form.getContent());

        redirectAttributes.addAttribute("boardType", boardType);
        redirectAttributes.addAttribute("id", boardId);

        if (bindingResult.hasErrors()) {
            log.info("댓글 작성 실패 {}", bindingResult.getAllErrors());
            return "redirect:/board/{boardType}/detail/{id}";
        }

        replyService.reply(loginMember.getId(), boardId, form);

        return "redirect:/board/{boardType}/detail/{id}";
    }

    @PostMapping("/{boardType}/{boardId}/auth/reply/delete/{replyId}")
    public String deleteReply(@PathVariable BoardType boardType,
                              @PathVariable Long boardId,
                              @PathVariable Long replyId,
                              RedirectAttributes redirectAttributes) {

        log.info("id = {}", replyId);

        replyService.deleteReply(replyId);

        redirectAttributes.addAttribute("boardType", boardType);
        redirectAttributes.addAttribute("id", boardId);

        return "redirect:/board/{boardType}/detail/{id}";
    }
}

package com.petmily.controller.board;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.enum_type.BoardType;
import com.petmily.domain.dto.PetmilyPage;
import com.petmily.domain.dto.board.BoardDetailForm;
import com.petmily.domain.dto.board.BoardListForm;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.dto.board.WriteBoardForm;
import com.petmily.domain.dto.reply.WriteReplyForm;
import com.petmily.domain.dto_converter.BoardDtoConverter;
import com.petmily.repository.BoardRepository;
import com.petmily.service.BoardService;
import com.petmily.service.PictureService;
import com.petmily.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
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

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final PictureService pictureService;
    private final BoardRepository boardRepository;
    private final BoardDtoConverter boardDtoConverter;

    @ResponseBody
    @GetMapping("/image/{fileStoreName}")
    public Resource getImage(@PathVariable String fileStoreName) throws MalformedURLException {
        return pictureService.findOne(fileStoreName);
    }

    @GetMapping("/{boardType}/list")
    public String list(@PathVariable BoardType boardType,
                       @PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {

        Page<Board> allBoards = boardRepository.findAllByBoardType(boardType, pageable);

        log.info("total element = {}", allBoards.getTotalElements());
        log.info("total pages = {}", allBoards.getTotalPages());

        Page<BoardListForm> mapToDto = allBoards.map(board -> boardDtoConverter.entityToDto(board, BoardListForm.class)
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다.")));

        PetmilyPage<BoardListForm> boardPage = new PetmilyPage<>(mapToDto);

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("boardPage", boardPage);

        return "/view/board/board_list";
    }

    @GetMapping("/{boardType}/detail/{boardId}")
    public String detailForm(@PathVariable BoardType boardType,
                             @PathVariable Long boardId,
                             Model model) {

        log.info("board type = {}, id = {}", boardType, boardId);

        Board board = boardService.findOne(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        BoardDetailForm boardForm = boardDtoConverter.entityToDto(board, BoardDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다."));

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("board", boardForm);
        model.addAttribute("writeReplyForm", new WriteReplyForm());

        return "/view/board/detail_form";
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

        ModifyBoardForm boardForm = boardDtoConverter.entityToDto(board, ModifyBoardForm.class)
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다."));

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("id", id);
        model.addAttribute("form", boardForm);

        return "/view/board/modify_form";
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

        BoardDetailForm boardForm = boardDtoConverter.entityToDto(board, BoardDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다."));

        Long replyId = replyService.reply(loginMember.getId(), boardId, form);

        Reply reply = replyService.findOne(replyId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

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

package com.petmily.controller.board;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.dto.PetmilyPage;
import com.petmily.domain.dto.board.BoardDetailForm;
import com.petmily.domain.dto.board.BoardListForm;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.dto.board.WriteBoardForm;
import com.petmily.domain.dto.board.find_watch.FindWatchBoardDetailForm;
import com.petmily.domain.dto.board.find_watch.FindWatchBoardListForm;
import com.petmily.domain.dto.board.find_watch.SearchCondition;
import com.petmily.domain.dto.reply.WriteReplyForm;
import com.petmily.domain.dto_converter.BoardDtoConverter;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BoardType;
import com.petmily.domain.enum_type.FindWatchBoardStatus;
import com.petmily.service.BoardService;
import com.petmily.service.PictureService;
import com.petmily.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final PictureService pictureService;
    private final BoardDtoConverter boardDtoConverter;
    private final MessageSource ms;

    @ResponseBody
    @GetMapping("/board/image/{fileStoreName}")
    public Resource getImage(@PathVariable String fileStoreName) throws MalformedURLException {
        return pictureService.findOne(fileStoreName);
    }

    @ModelAttribute("animalSpecies")
    public AnimalSpecies[] animalSpecies() {
        return AnimalSpecies.values();
    }

    @ModelAttribute("boardStatus")
    public FindWatchBoardStatus[] findWatchBoardStatuses() {
        return FindWatchBoardStatus.values();
    }

    @GetMapping("/board/{boardType}/list")
    public String list(@PathVariable BoardType boardType,
                       @PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                       @ModelAttribute("searchForm") SearchCondition searchCondition,
                       @RequestParam(required = false) Integer page,
                       @CookieValue(value = "species", required = false) String cookieSpecies,
                       @CookieValue(name = "boardStatus", required = false) String cookieBoardStatus,
                       @CookieValue(name = "keyword", required = false) String cookieKeyword,
                       HttpServletResponse response,
                       Model model) {

        if (page != null || hasCondition(searchCondition)) {
            checkSearchCondition(searchCondition, cookieSpecies, cookieBoardStatus, cookieKeyword, response);
        }

        Page<BoardListForm> boardListForm;
        Page<Board> allBoards = boardService.findAllByBoardType(boardType, searchCondition, pageable);

        if (isFindWatchBoard(boardType)) {
            boardListForm = convertToFindWatchBoardListForm(allBoards);
        } else {
            boardListForm = convertToBoardListForm(allBoards);
        }

        PetmilyPage<BoardListForm> boardPage = new PetmilyPage<>(boardListForm);

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("boardPage", boardPage);

        if (isFindWatchBoard(boardType)) {
            return "view/board/find_watch/board_list";
        } else {
            return "view/board/board_list";
        }
    }

    private void checkSearchCondition(SearchCondition searchCondition, String cookieSpecies, String cookieBoardStatus, String cookieKeyword, HttpServletResponse response) {
        if (!hasCondition(searchCondition)) {
            updateSearchCondition(searchCondition, cookieSpecies, cookieBoardStatus, cookieKeyword);
        } else {
            updateCookies(searchCondition, response);
        }
    }

    private boolean hasCondition(SearchCondition searchCondition) {
        return searchCondition.getSpecies() != null || searchCondition.getBoardStatus() != null || StringUtils.hasText(searchCondition.getKeyword());
    }

    private void updateSearchCondition(SearchCondition searchCondition, String cookieSpecies, String cookieBoardStatus, String cookieKeyword) {
        if (cookieSpecies != null) {
            searchCondition.setSpecies(AnimalSpecies.valueOf(cookieSpecies));
        }

        if (cookieBoardStatus != null) {
            searchCondition.setBoardStatus(FindWatchBoardStatus.valueOf(cookieBoardStatus));
        }

        searchCondition.setKeyword(cookieKeyword);
    }

    private void updateCookies(SearchCondition searchCondition, HttpServletResponse response) {
        Cookie speciesCookie;
        Cookie boardStatusCookie;
        Cookie keywordCookie;

        if (searchCondition.getSpecies() != null) {
            speciesCookie = new Cookie("species", searchCondition.getSpecies().name());
        } else {
            speciesCookie = removeCookie("species");
        }

        if (searchCondition.getBoardStatus() != null) {
            boardStatusCookie = new Cookie("boardStatus", searchCondition.getBoardStatus().name());
        } else {
            boardStatusCookie = removeCookie("boardStatus");
        }

        if (StringUtils.hasText(searchCondition.getKeyword())) {
            keywordCookie = new Cookie("keyword", searchCondition.getKeyword());
        } else {
            keywordCookie = removeCookie("keyword");
        }

        ArrayList<Cookie> cookies = new ArrayList<>(Arrays.asList(speciesCookie, boardStatusCookie, keywordCookie));

        cookies.stream()
                .forEach(cookie -> {
                    cookie.setPath("/");
                    response.addCookie(cookie);
                });
    }

    private static Cookie removeCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);

        return cookie;
    }

    private static boolean isFindWatchBoard(BoardType boardType) {
        return boardType.equals(BoardType.FIND) || boardType.equals(BoardType.WATCH);
    }

    private Page<BoardListForm> convertToFindWatchBoardListForm(Page<Board> allBoards) {
        return allBoards
                .map(board -> boardDtoConverter.entityToDto(board, FindWatchBoardListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert"))));
    }

    private Page<BoardListForm> convertToBoardListForm(Page<Board> allBoards) {
        return allBoards
                .map(board -> boardDtoConverter.entityToDto(board, BoardListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert"))));
    }

    @GetMapping("/board/{boardType}/detail/{boardId}")
    public String detailForm(@PathVariable BoardType boardType,
                             @PathVariable Long boardId,
                             Model model) {

        log.info("board type = {}, id = {}", boardType, boardId);

        BoardDetailForm boardDetailForm;

        Board board = boardService.findOne(boardId)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.member.null")));

        if (isFindWatchBoard(boardType)) {
            boardDetailForm = convertToFindWatchBoardDetailForm(board);
        } else {
            boardDetailForm = convertToBoardDetailForm(board);
        }

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("board", boardDetailForm);
        model.addAttribute("writeReplyForm", new WriteReplyForm());

        return "view/board/detail_form";
    }

    private BoardDetailForm convertToFindWatchBoardDetailForm(Board board) {
        return boardDtoConverter.entityToDto(board, FindWatchBoardDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));
    }

    private BoardDetailForm convertToBoardDetailForm(Board board) {
        return boardDtoConverter.entityToDto(board, BoardDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));
    }

    @GetMapping("/board/{boardType}/auth/write")
    public String writeForm(@PathVariable BoardType boardType, Model model) {
        log.info("board type = {}", boardType);

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("form", new WriteBoardForm());

        return "view/board/write_form";
    }

    @PostMapping("/board/{boardType}/auth/write")
    public String write(@PathVariable BoardType boardType,
                        @SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                        @ModelAttribute("form") @Valid WriteBoardForm form,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {

        log.info("write form data = {}", form);

        if (bindingResult.hasErrors()) {
            log.info("게시글 작성 실패 {}", bindingResult.getAllErrors());
            model.addAttribute("boardType", boardType.name().toLowerCase());

            return "view/board/write_form";
        }

        Long boardId = boardService.write(loginMember.getId(), boardType, form);

        redirectAttributes.addAttribute("boardType", boardType.name().toLowerCase());
        redirectAttributes.addAttribute("id", boardId);

        return "redirect:/board/{boardType}/detail/{id}";
    }

    @GetMapping("/board/{boardType}/auth/modify/{id}")
    public String modifyForm(@PathVariable BoardType boardType,
                             @PathVariable Long id,
                             Model model) {

        log.info("board type = {}, id = {}", boardType, id);

        Board board = boardService.findOne(id)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.board.null")));

        ModifyBoardForm modifyBoardForm = convertToModifyBoardForm(board);

        model.addAttribute("boardType", boardType.name().toLowerCase());
        model.addAttribute("id", id);
        model.addAttribute("form", modifyBoardForm);

        return "view/board/modify_form";
    }

    private ModifyBoardForm convertToModifyBoardForm(Board board) {
        return boardDtoConverter.entityToDto(board, ModifyBoardForm.class)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));
    }

    @PostMapping("/board/{boardType}/auth/modify/{id}")
    public String modify(@PathVariable BoardType boardType,
                         @PathVariable Long id,
                         @ModelAttribute("form") @Valid ModifyBoardForm form,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {

        if (bindingResult.hasErrors()) {
            log.info("게시글 수정 실패 {}", bindingResult.getAllErrors());
            model.addAttribute("boardType", boardType.name().toLowerCase());

            return "view/board/modify_form";
        }

        boardService.modifyBoardInfo(id, boardType, form);

        Board board = boardService.findOne(id)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.board.null")));

        log.info("게시글 수정 완료 {}", board);

        redirectAttributes.addAttribute("boardType", boardType.name().toLowerCase());
        redirectAttributes.addAttribute("id", id);

        return "redirect:/board/{boardType}/detail/{id}";
    }

    @GetMapping("/board/{boardType}/auth/delete/{id}")
    public String delete(@PathVariable BoardType boardType,
                         @PathVariable Long id,
                         RedirectAttributes redirectAttributes) {

        boardService.deleteBoard(id);

        log.info("게시글 삭제 완료");

        redirectAttributes.addAttribute("boardType", boardType.name().toLowerCase());
        redirectAttributes.addAttribute("id", id);

        return "redirect:/board/{boardType}/list";
    }

    @PostMapping("/board/{boardType}/{boardId}/auth/reply/write")
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

    @PostMapping("/board/{boardType}/{boardId}/auth/reply/delete/{replyId}")
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

    @GetMapping("/member/auth/board/list")
    public String memberBoardList(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                                  Model model) {

        List<Board> boards = boardService.findAll(loginMember);

        List<BoardListForm> forms = boards.stream()
                .map(board -> boardDtoConverter.entityToDto(board, BoardListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert"))))
                .collect(Collectors.toList());

        model.addAttribute("forms", forms);

        return "view/member/board_list";
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}

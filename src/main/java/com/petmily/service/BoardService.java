package com.petmily.service;

import com.petmily.domain.builder.board.BoardBuilder;
import com.petmily.domain.builder.board.FindWatchBoardBuilder;
import com.petmily.domain.builder.board.MatchInfoBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.core.board.FindWatchBoard;
import com.petmily.domain.dto.board.ModifyBoardApiForm;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.dto.board.WriteBoardForm;
import com.petmily.domain.dto.board.find_watch.SearchCondition;
import com.petmily.domain.enum_type.BoardType;
import com.petmily.domain.enum_type.FindWatchBoardStatus;
import com.petmily.repository.BoardRepository;
import com.petmily.repository.MatchInfoRepository;
import com.petmily.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final PictureService pictureService;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MatchInfoRepository matchInfoRepository;
    private final MessageSource ms;

    // 게시글 등록
    @Transactional
    public Long write(Long memberId, BoardType boardType, WriteBoardForm form) {
        Member member = getMember(memberId);
        Board board = writeBoard(boardType, form, member);

        if (hasPicture(form.getPictures())) {
            pictureService.store(form.getPictures(), board);
        }

        boardRepository.save(board);

        if (isFindWatchBoard(boardType) && hasMatchBoard(board)) {
            createMatchInfo((FindWatchBoard) board);
        }

        return board.getId();
    }

    private boolean hasMatchBoard(Board board) {
        FindWatchBoard findWatchBoard = (FindWatchBoard) board;

        List<Long> matchedBoardIds = boardRepository.matchWithFindWatchBoard(findWatchBoard);

        return !matchedBoardIds.isEmpty();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.member.null")));
    }

    private Board writeBoard(BoardType boardType, WriteBoardForm form, Member member) {
        Board board;

        if (!StringUtils.hasText(form.getAnimalKind())) {
            form.setAnimalKind(null);
            log.info("empty animal kind = {}", form.getAnimalKind());
        }

        if (!StringUtils.hasText(form.getAnimalName())) {
            form.setAnimalName(null);
            log.info("empty animal name = {}", form.getAnimalName());
        }

        if (isFindWatchBoard(boardType)) {
            board = makeFindWatchBoard(boardType, form, member);
        } else {
            board = makeBoard(boardType, form, member);
        }

        return board;
    }

    private Board makeBoard(BoardType boardType, WriteBoardForm form, Member member) {
        return new BoardBuilder(member, boardType)
                .setTitle(form.getTitle())
                .setContent(form.getContent())
                .setShownAll(form.getShownAll())
                .build();
    }

    private FindWatchBoard makeFindWatchBoard(BoardType boardType, WriteBoardForm form, Member member) {
        return new FindWatchBoardBuilder(member, boardType)
                .setTitle(form.getTitle())
                .setContent(form.getContent())
                .setShownAll(form.getShownAll())
                .setLostOrWatchTime(form.getLostOrWatchTime())
                .setSpecies(form.getSpecies())
                .setAnimalName(form.getAnimalName())
                .setAnimalKind(form.getAnimalKind())
                .setAnimalAge(form.getAnimalAge())
                .setAnimalWeight(form.getAnimalWeight())
                .build();
    }

    private boolean isFindWatchBoard(BoardType boardType) {
        return boardType.equals(BoardType.FIND) || boardType.equals(BoardType.WATCH);
    }

    private boolean hasPicture(List<MultipartFile> form) {
        if (form == null) {
            return false;
        }

        return !form.get(0).isEmpty();
    }

    // 게시글 조회
    public Optional<Board> findOne(Long id) {
        return boardRepository.findById(id);
    }

    // 전체 게시글 조회
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    // 회원으로 전체 게시글 조회
    public List<Board> findAll(Member member) {
        return boardRepository.findAllByMemberOrderByBoardType(member);
    }

    // 게시글 수정
    @Transactional
    public Long modifyBoardInfo(Long id, BoardType boardType, ModifyBoardForm form) {
        Board board = getBoard(id);

        board.getPictures()
                .forEach(pictureService::delete);

        if (hasPicture(form.getPictures())) {
            pictureService.store(form.getPictures(), board);
        }

        if (!StringUtils.hasText(form.getAnimalKind())) {
            form.setAnimalKind(null);
        }

        if (!StringUtils.hasText(form.getAnimalName())) {
            form.setAnimalName(null);
        }

        if (isFindWatchBoard(boardType)) {
            FindWatchBoard findWatchBoard = (FindWatchBoard) board;

            findWatchBoard.changeInfo(form);
            updateMatchInfo(findWatchBoard);
        } else {
            board.changeInfo(form);
        }

        return board.getId();
    }

    @Transactional
    public Long modifyBoardInfo(Long id, ModifyBoardApiForm apiForm) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.member.null")));

        board.changeInfo(apiForm);

        return board.getId();
    }

    private void updateMatchInfo(FindWatchBoard findWatchBoard) {
        removeMatchInfo(findWatchBoard);

        if (hasMatchBoard(findWatchBoard)) {
            createMatchInfo(findWatchBoard);
        }
    }

    private void removeMatchInfo(FindWatchBoard findWatchBoard) {
        deleteMatchInfo(findWatchBoard);

        List<FindWatchBoard> matchBoards = findWatchBoard.getAllMatchBoards();
        List<Long> needUpdateIds = matchBoards.stream()
                .filter(board -> board.getAllMatchBoards().isEmpty())
                .map(Board::getId)
                .collect(Collectors.toList());

        needUpdateIds.add(findWatchBoard.getId());

        boardRepository.updateBoardStatus(needUpdateIds, FindWatchBoardStatus.LOST);
    }

    private void deleteMatchInfo(FindWatchBoard findWatchBoard) {
        List<FindWatchBoard> allMatchBoards = findWatchBoard.getAllMatchBoards();
        allMatchBoards.forEach(matchBoard -> matchBoard.deleteMatchInfo(findWatchBoard));

        List<Long> allMatchInfoIds = findWatchBoard.getAllMatchInfoIds();
        allMatchInfoIds.forEach(matchInfoRepository::deleteById);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = getBoard(boardId);

        board.getPictures().forEach(pictureService::delete);

        if (isFindWatchBoard(board.getBoardType()) && hasMatchBoard(board)) {
            removeMatchInfo((FindWatchBoard) board);
        }

        boardRepository.deleteById(boardId);
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }

    public Page<Board> findAllByBoardType(BoardType boardType, SearchCondition searchCondition, Pageable pageable) {
        if (isFindWatchBoard(boardType)) {
            return boardRepository.findAllByBoardType(boardType, searchCondition, pageable);
        } else {
            return boardRepository.findAllByBoardType(boardType, pageable);
        }
    }

    @Transactional
    public void createMatchInfo(FindWatchBoard findWatchBoard) {
        List<Long> matchedBoardIds = boardRepository.matchWithFindWatchBoard(findWatchBoard);
        List<FindWatchBoard> matchBoards = getMatchBoards(matchedBoardIds);

        match(findWatchBoard, matchBoards);

        List<Long> needUpdateIds = makeNeedUpdatedIds(findWatchBoard, matchedBoardIds);
        Long countUpdatedBoard = boardRepository.updateBoardStatus(needUpdateIds, FindWatchBoardStatus.MATCH);

        log.info("number of update board = {}", countUpdatedBoard);
    }

    private List<FindWatchBoard> getMatchBoards(List<Long> matchedBoardIds) {
        List<FindWatchBoard> matchBoards = matchedBoardIds.stream()
                .map(boardId -> (FindWatchBoard) getBoard(boardId))
                .collect(Collectors.toList());
        return matchBoards;
    }

    private List<Long> makeNeedUpdatedIds(FindWatchBoard targetBoard, List<Long> matchedBoardIds) {
        List<Long> needUpdateIds = new ArrayList<>();

        needUpdateIds.add(targetBoard.getId());
        needUpdateIds.addAll(matchedBoardIds);

        return needUpdateIds;
    }

    private void match(FindWatchBoard targetBoard, List<FindWatchBoard> matchedBoards) {
        matchedBoards.stream()
                .map(matchedBoard -> new MatchInfoBuilder(targetBoard, matchedBoard).build())
                .forEach(matchInfo -> matchInfoRepository.save(matchInfo));
    }

    private Board getBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.board.null")));
    }

    public List<FindWatchBoard> findBoardStatusMatch(Long memberId) {
        Member member = getMember(memberId);

        return member.getBoards().stream()
                .filter(board -> isFindWatchBoard(board.getBoardType()))
                .map(board -> (FindWatchBoard) board)
                .filter(findWatchBoard -> isBoardStatusMatch(findWatchBoard))
                .collect(Collectors.toList());
    }

    private boolean isBoardStatusMatch(FindWatchBoard findWatchBoard) {
        return findWatchBoard.getBoardStatus().equals(FindWatchBoardStatus.MATCH);
    }
}

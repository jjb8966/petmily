package com.petmily.service;

import com.petmily.builder.BoardBuilder;
import com.petmily.builder.ReplyBuilder;
import com.petmily.domain.AbandonedAnimal;
import com.petmily.domain.Board;
import com.petmily.domain.Member;
import com.petmily.domain.Reply;
import com.petmily.domain.application.Application;
import com.petmily.dto.board.ChangeBoardDto;
import com.petmily.dto.board.WriteBoardDto;
import com.petmily.enum_type.BoardType;
import com.petmily.repository.BoardRepository;
import com.petmily.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ReplyService replyService;

    // 게시글 등록
    public Long write(Long memberId, WriteBoardDto boardDto) {
        Member member = getMember(memberId);

        Board board = new BoardBuilder(member, BoardType.FREE)
                .setTitle(boardDto.getTitle())
                .setContent(boardDto.getContent())
                .setPictures(boardDto.getPictures())
                .setReplies(boardDto.getReplies())
                .setShownAll(boardDto.isShownAll())
                .setWriteTime(LocalDateTime.now())
                .build();

        boardRepository.save(board);

        return board.getId();
    }

    // 게시글 조회
    public Optional<Board> findOne(Long id) {
        return boardRepository.findById(id);
    }

    // 전체 게시글 조회
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    // 게시글 수정
    public Long changeBoardInfo(Long id, ChangeBoardDto boardDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        board.changeInfo(boardDto);

        return board.getId();
    }

    // 게시글 삭제
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        board.getMember()
                .getBoards()
                .removeIf(b -> b.getId().equals(id));

        deleteReplyAboutBoard(id);

        boardRepository.deleteById(id);
    }

    private void deleteReplyAboutBoard(Long boardId) {
        replyService.findAll().stream()
                .filter(reply -> reply.getBoard().getId().equals(boardId))
                .forEach(reply -> replyService.deleteReply(reply.getId()));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}

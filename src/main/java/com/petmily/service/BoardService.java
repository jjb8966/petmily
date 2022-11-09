package com.petmily.service;

import com.petmily.domain.builder.BoardBuilder;
import com.petmily.domain.builder.PictureBuilder;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.enum_type.BoardType;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.dto.board.WriteBoardForm;
import com.petmily.repository.BoardRepository;
import com.petmily.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    @Value("${file.dir}")
    private String storePath;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ReplyService replyService;

    // 게시글 등록
    public Long write(Long memberId, BoardType boardType, WriteBoardForm form) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Board board = new BoardBuilder(member, boardType)
                .setTitle(form.getTitle())
                .setContent(form.getContent())
                .setShownAll(form.getShownAll())
                .build();

        if (hasPicture(form.getPictures())) {
            storePicture(form.getPictures(), board);
        }

        boardRepository.save(board);

        return board.getId();
    }

    private static boolean hasPicture(List<MultipartFile> form) {
        if (form == null) {
            return false;
        }

        return !form.get(0).isEmpty();
    }

    private void storePicture(List<MultipartFile> multipartFiles, Board board) {
        multipartFiles.stream().forEach(multipartFile ->
                new PictureBuilder()
                        .setBoard(board)
                        .store(multipartFile, storePath)
                        .build());
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
    public Long modifyBoardInfo(Long id, ModifyBoardForm form) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        board.clearPicture();

        if (hasPicture(form.getPictures())) {
            storePicture(form.getPictures(), board);
        }

        board.changeInfo(form);

        return board.getId();
    }

    // 게시글 삭제
    public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

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

}

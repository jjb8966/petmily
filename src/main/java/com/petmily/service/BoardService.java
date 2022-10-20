package com.petmily.service;

import com.petmily.domain.Board;
import com.petmily.domain.dto.board.ChangeBoardDto;
import com.petmily.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository repository;

    // 게시글 등록
    public Long write(Board board) {
        repository.save(board);

        return board.getId();
    }

    // 게시글 조회
    public Optional<Board> findOne(Long id) {
        return repository.findById(id);
    }

    // 전체 게시글 조회
    public List<Board> findAll() {
        return repository.findAll();
    }

    // 게시글 수정
    public Long changeBoardInfo(Long id, ChangeBoardDto boardDto) {
        Board board = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        board.changeInfo(boardDto);

        return board.getId();
    }

    // 게시글 삭제
    public void deleteBoard(Long id) {
        repository.deleteById(id);
    }
}

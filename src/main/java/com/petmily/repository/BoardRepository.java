package com.petmily.repository;

import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.enum_type.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Override
    @EntityGraph(attributePaths = {"pictures"})
    Optional<Board> findById(Long aLong);

    Page<Board> findAll(Pageable pageable);

    List<Board> findAllByMemberOrderByBoardType(Member member);

    Page<Board> findAllByBoardType(BoardType boardType, Pageable pageable);
}

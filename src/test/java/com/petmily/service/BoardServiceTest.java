package com.petmily.service;

import com.petmily.domain.Board;
import com.petmily.domain.Member;
import com.petmily.domain.Reply;
import com.petmily.domain.builder.BoardBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.dto.board.ChangeBoardDto;
import com.petmily.domain.embedded_type.Picture;
import com.petmily.domain.enum_type.BoardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BoardServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    BoardService service;

    private Member member;

    @BeforeEach
    void before() {
        member = new MemberBuilder("memberA", "123").build();
        em.persist(member);
    }

    @Test
    @DisplayName("게시글을 등록하고 id를 통한 조회를 할 수 있다.")
    void register_findOne() {
        //given
        Board board = new BoardBuilder(member, BoardType.FREE)
                .setReplies(new ArrayList<Reply>())
                .setPictures(new ArrayList<Picture>())
                .setTitle("title")
                .setContent("content")
                .setShownAll(true)
                .setWriteTime(LocalDateTime.now())
                .build();

        //when
        service.write(board);
        Board findBoard = service.findOne(board.getId()).orElseThrow();

        //then
        assertThat(findBoard.getTitle()).isEqualTo("title");
        assertThat(findBoard).isEqualTo(board);
    }

    @Test
    @DisplayName("모든 게시글을 조회할 수 있다.")
    void findAll() {
        //given
        Board boardA = new BoardBuilder(member, BoardType.FREE).setTitle("A").build();
        Board boardB = new BoardBuilder(member, BoardType.FREE).setTitle("B").build();
        Board boardC = new BoardBuilder(member, BoardType.FREE).setTitle("C").build();
        Board boardD = new BoardBuilder(member, BoardType.FREE).setTitle("D").build();
        service.write(boardA);
        service.write(boardB);
        service.write(boardC);
        service.write(boardD);

        //when
        List<Board> allBoards = service.findAll();

        //then
        assertThat(allBoards.size()).isEqualTo(4);
        assertThat(allBoards).containsExactly(boardA, boardB, boardC, boardD);
    }

    @Test
    @DisplayName("게시글의 정보를 변경할 수 있다.")
    void changeAnimalInfo() {
        //given
        Board board = new BoardBuilder(member, BoardType.FREE).setTitle("A").build();
        service.write(board);

        ChangeBoardDto boardDto = new ChangeBoardDto();
        boardDto.setTitle("B");

        //when
        service.changeBoardInfo(board.getId(), boardDto);

        //then
        assertThat(board.getTitle()).isEqualTo("B");
    }

    @Test
    @DisplayName("게시글을 삭제할 수 있다.")
    void deleteAnimal() {
        //given
        Board board = new BoardBuilder(member, BoardType.FREE).setTitle("A").build();
        service.write(board);

        //when
        service.deleteBoard(board.getId());

        //then
        boolean isPresent = service.findOne(board.getId()).isPresent();
        assertThat(isPresent).isFalse();
    }
}
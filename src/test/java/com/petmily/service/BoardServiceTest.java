package com.petmily.service;

import com.petmily.domain.builder.BoardBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.ReplyBuilder;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.enum_type.BoardType;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.dto.board.WriteBoardForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class BoardServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberService memberService;

    @Autowired
    BoardService boardService;

    @Autowired
    ReplyService replyService;

    @Test
    @DisplayName("게시글을 등록하고 id를 통한 조회를 할 수 있다.")
    void write() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        em.persist(member);

        //when
        WriteBoardForm writeBoardForm = new WriteBoardForm();
        writeBoardForm.setTitle("title");
        writeBoardForm.setContent("content");
        writeBoardForm.setShownAll(true);

        Long boardId = boardService.write(member.getId(), BoardType.FREE, writeBoardForm);
        Board findBoard = em.find(Board.class, boardId);

        //then
        assertThat(findBoard.getTitle()).isEqualTo(writeBoardForm.getTitle());
        assertThat(findBoard.getContent()).isEqualTo(writeBoardForm.getContent());
        assertThat(findBoard.getShownAll()).isEqualTo(writeBoardForm.getShownAll());
    }

    @Test
    @DisplayName("게시글을 조회할 수 있다.")
    void findOne() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        Board board = new BoardBuilder(member, BoardType.FREE).setTitle("title").build();
        em.persist(member);

        //when
        Optional<Board> findBoardOptional = boardService.findOne(board.getId());

        //then
        assertThat(findBoardOptional.isPresent()).isTrue();
        assertThat(findBoardOptional.get().getTitle()).isEqualTo(board.getTitle());
    }

    @Test
    @DisplayName("모든 게시글을 조회할 수 있다.")
    void findAll() {
        //given
        Member memberA = new MemberBuilder("memberA", "123").build();
        Member memberB = new MemberBuilder("memberB", "123").build();
        Board boardA = new BoardBuilder(memberA, BoardType.FREE).setTitle("titleA").build();
        Board boardB = new BoardBuilder(memberA, BoardType.FREE).setTitle("titleB").build();
        Board boardC = new BoardBuilder(memberB, BoardType.FREE).setTitle("titleC").build();

        em.persist(memberA);
        em.persist(memberB);

        //when
        List<Board> allBoards = boardService.findAll();

        //then
        assertThat(allBoards).hasSize(3);
        assertThat(allBoards).containsExactly(boardA, boardB, boardC);
    }

    @Test
    @DisplayName("특정 회원의 전체 게시글을 조회할 수 있다.")
    void findAll_about_member() {
        //given
        Member memberA = new MemberBuilder("memberA", "123").build();
        Member memberB = new MemberBuilder("memberB", "123").build();
        Board boardA = new BoardBuilder(memberA, BoardType.FREE).setTitle("titleA").build();
        Board boardB = new BoardBuilder(memberA, BoardType.FREE).setTitle("titleB").build();
        Board boardC = new BoardBuilder(memberB, BoardType.FREE).setTitle("titleC").build();

        em.persist(memberA);
        em.persist(memberB);

        //when
        List<Board> allBoardsAboutMemberA = boardService.findAll(memberA);

        //then
        assertThat(allBoardsAboutMemberA).hasSize(2);
        assertThat(allBoardsAboutMemberA).containsExactly(boardA, boardB);
    }

    @Test
    @DisplayName("게시글의 정보를 변경할 수 있다.")
    void changeBoardInfo() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        Board board = new BoardBuilder(member, BoardType.FREE)
                .setTitle("titleA")
                .setContent("contentA")
                .setShownAll(true)
                .build();

        em.persist(member);

        //when
        ModifyBoardForm modifyBoardForm = new ModifyBoardForm();
        modifyBoardForm.setTitle("titleB");
        modifyBoardForm.setContent("contentB");
        modifyBoardForm.setShownAll(false);

        boardService.modifyBoardInfo(board.getId(), modifyBoardForm);
        Board findBoard = em.find(Board.class, board.getId());

        //then
        assertThat(findBoard.getTitle()).isEqualTo(modifyBoardForm.getTitle());
        assertThat(findBoard.getContent()).isEqualTo(modifyBoardForm.getContent());
        assertThat(findBoard.getShownAll()).isEqualTo(modifyBoardForm.getShownAll());
    }

    @Test
    @DisplayName("게시글을 삭제할 수 있다.")
    void deleteBoard() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        Board board = new BoardBuilder(member, BoardType.FREE).build();

        em.persist(member);

        //when
        boardService.deleteBoard(board.getId());
        Board findBoard = em.find(Board.class, board.getId());

        //then
        assertThat(findBoard).isNull();
    }

    @Test
    @DisplayName("게시글을 삭제하면 게시글에 달린 모든 댓글은 삭제된다.")
    void deleteBoard_with_reply() {
        //given
        Member memberA = new MemberBuilder("memberA", "123").build();
        Member memberB = new MemberBuilder("memberB", "123").build();
        Board board = new BoardBuilder(memberA, BoardType.FREE).build();
        Reply replyA = new ReplyBuilder(memberA, board).setContent("replyA").build();
        Reply replyB = new ReplyBuilder(memberB, board).setContent("replyB").build();

        em.persist(memberA);

        //when
        boardService.deleteBoard(board.getId());
        Reply findReplyA = em.find(Reply.class, replyA.getId());
        Reply findReplyB = em.find(Reply.class, replyB.getId());

        //then
        assertThat(findReplyA).isNull();
        assertThat(findReplyB).isNull();
    }
}
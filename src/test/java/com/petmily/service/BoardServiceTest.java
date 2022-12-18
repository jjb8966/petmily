package com.petmily.service;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.ReplyBuilder;
import com.petmily.domain.builder.board.BoardBuilder;
import com.petmily.domain.builder.board.FindWatchBoardBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.core.board.FindWatchBoard;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.dto.board.WriteBoardForm;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BoardType;
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
    BoardService boardService;

    @Test
    @DisplayName("게시글을 등록할 수 있다.")
    void write() {
        //given
        Member member = new MemberBuilder("member", "123").build();

        em.persist(member);

        WriteBoardForm writeBoardForm = new WriteBoardForm();
        writeBoardForm.setTitle("title");
        writeBoardForm.setContent("content");
        writeBoardForm.setShownAll(true);

        //when
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

        ModifyBoardForm modifyBoardForm = new ModifyBoardForm();
        modifyBoardForm.setTitle("titleB");
        modifyBoardForm.setContent("contentB");
        modifyBoardForm.setShownAll(false);

        //when
        boardService.modifyBoardInfo(board.getId(), board.getBoardType(), modifyBoardForm);
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

    @Test
    @DisplayName("찾아요/봤어요 게시글 등록 시 정보가 일치하는 게시글과 매칭된다.")
    void updateMatchInfo() {
        //given
        Member memberA = new MemberBuilder("memberA", "123").build();
        Member memberB = new MemberBuilder("memberB", "123").build();

        FindWatchBoard find1 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard1")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard find2 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard2")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard find3 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard3")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard watch = new FindWatchBoardBuilder(memberB, BoardType.WATCH)
                .setTitle("watchBoard")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        em.persist(memberA);
        em.persist(memberB);

        //when
        boardService.createMatchInfo(watch);

        FindWatchBoard find4 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard4")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        em.persist(find4);

        // findBoard4 -> watchBoard
        boardService.createMatchInfo(find4);

        em.flush();
        em.clear();

        FindWatchBoard findBoard1 = em.find(FindWatchBoard.class, find1.getId());
        FindWatchBoard findBoard2 = em.find(FindWatchBoard.class, find2.getId());
        FindWatchBoard findBoard3 = em.find(FindWatchBoard.class, find3.getId());
        FindWatchBoard findBoard4 = em.find(FindWatchBoard.class, find4.getId());
        FindWatchBoard watchBoard = em.find(FindWatchBoard.class, watch.getId());

        //then
        assertThat(watchBoard.getAllMatchBoards()).hasSize(4);
        assertThat(watchBoard.getAllMatchBoards()).contains(findBoard1, findBoard2, findBoard3, findBoard4);

        assertThat(findBoard1.getAllMatchBoards()).containsExactly(watchBoard);
        assertThat(findBoard2.getAllMatchBoards()).containsExactly(watchBoard);
        assertThat(findBoard3.getAllMatchBoards()).containsExactly(watchBoard);
        assertThat(findBoard4.getAllMatchBoards()).containsExactly(watchBoard);
    }

    @Test
    @DisplayName("사용자가 작성한 찾아요/봤어요 게시글 중 매칭된 게시글을 조회할 수 있다.")
    void findBoardStatusMatch() {
        //given
        Member memberA = new MemberBuilder("memberA", "123").build();
        Member memberB = new MemberBuilder("memberB", "123").build();

        FindWatchBoard find1 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard1")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard find2 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard2")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard find3 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard3")
                .setSpecies(AnimalSpecies.DOG)
                .build();

        FindWatchBoard watch1 = new FindWatchBoardBuilder(memberB, BoardType.WATCH)
                .setTitle("watchBoard1")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard watch2 = new FindWatchBoardBuilder(memberB, BoardType.WATCH)
                .setTitle("watchBoard2")
                .setSpecies(AnimalSpecies.DOG)
                .build();

        em.persist(memberA);
        em.persist(memberB);

        // 벌크성 쿼리
        boardService.createMatchInfo(watch1);
        boardService.createMatchInfo(watch2);

        em.flush();
        em.clear();

        //when
        List<FindWatchBoard> matchBoards = boardService.findBoardStatusMatch(memberA.getId());

        //then
        assertThat(matchBoards).hasSize(3);
    }

    @Test
    @DisplayName("찾아요/봤어요 게시글을 수정하면 매칭 게시글 정보도 수정된다.")
    void modifyBoardInfo() {
        //given
        Member memberA = new MemberBuilder("memberA", "123").build();
        Member memberB = new MemberBuilder("memberB", "123").build();

        FindWatchBoard find1 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard1")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard find2 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard2")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard find3 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard3")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard watch = new FindWatchBoardBuilder(memberB, BoardType.WATCH)
                .setTitle("watchBoard")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        FindWatchBoard find4 = new FindWatchBoardBuilder(memberA, BoardType.FIND)
                .setTitle("findBoard4")
                .setAnimalAge(3)
                .build();

        em.persist(memberA);
        em.persist(memberB);

        boardService.createMatchInfo(watch);

        em.flush();
        em.clear();

        FindWatchBoard watchBoard = em.find(FindWatchBoard.class, watch.getId());

        //when
        ModifyBoardForm modifyBoardForm = new ModifyBoardForm();
        modifyBoardForm.setTitle("new title");
        modifyBoardForm.setContent("new content");
        modifyBoardForm.setAnimalAge(3);

        boardService.modifyBoardInfo(watchBoard.getId(), BoardType.WATCH, modifyBoardForm);

        em.flush();
        em.clear();

        FindWatchBoard modifiedWatchBoard = em.find(FindWatchBoard.class, watch.getId());
        FindWatchBoard findBoard4 = em.find(FindWatchBoard.class, find4.getId());

        //then
        assertThat(modifiedWatchBoard.getAllMatchBoards()).hasSize(1);
        assertThat(modifiedWatchBoard.getAllMatchBoards()).containsExactly(findBoard4);
    }

}
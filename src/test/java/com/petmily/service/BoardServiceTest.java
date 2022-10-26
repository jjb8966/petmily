package com.petmily.service;

import com.petmily.builder.MemberBuilder;
import com.petmily.domain.Board;
import com.petmily.domain.Member;
import com.petmily.dto.board.ChangeBoardDto;
import com.petmily.dto.board.WriteBoardDto;
import com.petmily.dto.reply.ReplyDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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
    void write_findOne() {
        //given
        Member member = new MemberBuilder("memberA", "aaa").build();
        memberService.join(member);

        WriteBoardDto boardDto = new WriteBoardDto();
        boardDto.setTitle("title A");

        //when
        Long boardId = boardService.write(member.getId(), boardDto);
        Board findBoard = boardService.findOne(boardId).orElseThrow();

        //then
        assertThat(findBoard.getTitle()).isEqualTo("title A");
        assertThat(findBoard.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("모든 게시글을 조회할 수 있다.")
    void findAll() {
        //given
        Member member = new MemberBuilder("memberA", "aaa").build();
        memberService.join(member);

        Long a = boardService.write(member.getId(), new WriteBoardDto());
        Long b = boardService.write(member.getId(), new WriteBoardDto());
        Long c = boardService.write(member.getId(), new WriteBoardDto());
        Long d = boardService.write(member.getId(), new WriteBoardDto());

        //when
        List<Board> allBoards = boardService.findAll();
        Board boardA = boardService.findOne(a).orElseThrow();
        Board boardB = boardService.findOne(b).orElseThrow();
        Board boardC = boardService.findOne(c).orElseThrow();
        Board boardD = boardService.findOne(d).orElseThrow();

        //then
        assertThat(allBoards.size()).isEqualTo(4);
        assertThat(allBoards).containsExactly(boardA, boardB, boardC, boardD);
    }

    @Test
    @DisplayName("게시글의 정보를 변경할 수 있다.")
    void changeBoardInfo() {
        //given
        Member member = new MemberBuilder("memberA", "aaa").build();
        memberService.join(member);

        WriteBoardDto writeBoardDto = new WriteBoardDto();
        writeBoardDto.setTitle("A");

        Long boardId = boardService.write(member.getId(), writeBoardDto);

        ChangeBoardDto changeBoardDto = new ChangeBoardDto();
        changeBoardDto.setTitle("B");

        //when
        boardService.changeBoardInfo(boardId, changeBoardDto);
        Board board = boardService.findOne(boardId).orElseThrow();

        //then
        assertThat(board.getTitle()).isEqualTo("B");
    }

    @Test
    @DisplayName("게시글을 삭제할 수 있다.")
    void deleteBoard() {
        //given
        Member member = new MemberBuilder("memberA", "aaa").build();
        memberService.join(member);

        Long boardId = boardService.write(member.getId(), new WriteBoardDto());

        //when
        boardService.deleteBoard(boardId);

        //then
        assertThat(boardService.findOne(boardId).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("게시글을 삭제하면 게시글에 달린 모든 댓글은 삭제된다.")
    void deleteBoard2() {
        //given
        Member member1 = new MemberBuilder("aaa", "aaa").build();
        Member member2 = new MemberBuilder("bbb", "bbb").build();
        memberService.join(member1);
        memberService.join(member2);

        Long boardId = boardService.write(member1.getId(), new WriteBoardDto());

        replyService.reply(member1.getId(), boardId, new ReplyDto("1"));
        replyService.reply(member2.getId(), boardId, new ReplyDto("2"));
        replyService.reply(member1.getId(), boardId, new ReplyDto("3"));

        assertThat(replyService.findAll().size()).isEqualTo(3);
        assertThat(member1.getReplies().size()).isEqualTo(2);
        assertThat(member2.getReplies().get(0).getContent()).isEqualTo("2");

        //when
        boardService.deleteBoard(boardId);

        //then
        assertThat(replyService.findAll().size()).isEqualTo(0);
        assertThat(replyService.findAll().isEmpty()).isTrue();
        assertThat(member1.getReplies().isEmpty()).isTrue();
        assertThat(member2.getReplies().isEmpty()).isTrue();
    }
}
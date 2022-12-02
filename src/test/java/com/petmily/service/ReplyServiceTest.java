package com.petmily.service;

import com.petmily.domain.builder.BoardBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.ReplyBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.dto.reply.WriteReplyForm;
import com.petmily.domain.enum_type.BoardType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class ReplyServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    ReplyService replyService;

    @Autowired
    MemberService memberService;

    @Autowired
    BoardService boardService;

    Member member;
    Board board;

    @BeforeEach
    void beforeEach() {
        member = new MemberBuilder("member", "123").build();
        board = new BoardBuilder(member, BoardType.FREE).build();
        em.persist(member);
    }

    @Test
    @DisplayName("회원은 게시글에 댓글을 쓸 수 있다.")
    void reply() {
        //given
        WriteReplyForm writeReplyForm = new WriteReplyForm();
        writeReplyForm.setContent("reply");

        //when
        Long replyId = replyService.reply(member.getId(), board.getId(), writeReplyForm);
        Reply findReply = em.find(Reply.class, replyId);

        //then
        assertThat(findReply.getContent()).isEqualTo(writeReplyForm.getContent());
    }

    @Test
    @DisplayName("댓글을 삭제할 수 있다.")
    void deleteReply() {
        //given
        Reply reply = new ReplyBuilder(member, board).build();
        em.persist(reply);

        //when
        replyService.deleteReply(reply.getId());
        Reply findReply = em.find(Reply.class, reply.getId());

        //then
        assertThat(findReply).isNull();
    }
}
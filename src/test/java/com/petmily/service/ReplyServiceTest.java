package com.petmily.service;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import com.petmily.domain.dto.board.WriteBoardDto;
import com.petmily.domain.dto.reply.ChangeReplyDto;
import com.petmily.domain.dto.reply.ReplyDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;

    @Autowired
    MemberService memberService;

    @Autowired
    BoardService boardService;

    @Test
    @DisplayName("댓글 등록 및 id를 통한 조회를 할 수 있다.")
    void register_findOne() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        memberService.join(member);

        WriteBoardDto writeBoardDto = new WriteBoardDto();
        writeBoardDto.setTitle("hello");

        Long boardId = boardService.write(member.getId(), writeBoardDto);

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("hi");

        //when
        Long replyId = replyService.reply(member.getId(), boardId, replyDto);

        Reply reply = replyService.findOne(replyId).orElseThrow();
        Board board = boardService.findOne(boardId).orElseThrow();

        //then
        assertThat(reply.getMember().getLoginId()).isEqualTo(member.getLoginId());
        assertThat(reply.getBoard().getTitle()).isEqualTo(board.getTitle());
        assertThat(member.getReplies().contains(reply)).isTrue();
        assertThat(board.getReplies().contains(reply)).isTrue();
    }

    @Test
    @DisplayName("모든 댓글을 조회할 수 있다.")
    void findAll() {
        //given
        Member memberA = new MemberBuilder("memberA", "123").build();
        Member memberB = new MemberBuilder("memberB", "123").build();
        memberService.join(memberA);
        memberService.join(memberB);

        Long boardAId = boardService.write(memberA.getId(), new WriteBoardDto());
        Long boardBId = boardService.write(memberB.getId(), new WriteBoardDto());

        //when
        replyService.reply(memberB.getId(), boardAId, new ReplyDto());
        replyService.reply(memberA.getId(), boardAId, new ReplyDto());
        replyService.reply(memberB.getId(), boardAId, new ReplyDto());

        replyService.reply(memberA.getId(), boardBId, new ReplyDto());
        replyService.reply(memberB.getId(), boardBId, new ReplyDto());
        replyService.reply(memberA.getId(), boardBId, new ReplyDto());
        replyService.reply(memberB.getId(), boardBId, new ReplyDto());

        Board boardA = boardService.findOne(boardAId).orElseThrow();
        Board boardB = boardService.findOne(boardBId).orElseThrow();

        List<Reply> allReplies = replyService.findAll();

        //then
        assertThat(boardA.getReplies().size()).isEqualTo(3);
        assertThat(boardB.getReplies().size()).isEqualTo(4);
        assertThat(allReplies.size()).isEqualTo(7);

    }

    @Test
    @DisplayName("댓글 내용을 변경할 수 있다.")
    void changeReplyInfo() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        memberService.join(member);

        Long boardId = boardService.write(member.getId(), new WriteBoardDto());

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("old reply");

        Long replyId = replyService.reply(member.getId(), boardId, replyDto);
        LocalDateTime firstWriteTime = replyService.findOne(replyId).orElseThrow().getWriteTime();

        //when
        ChangeReplyDto changeDto = new ChangeReplyDto();
        changeDto.setContent("new reply");

        replyService.changeReplyInfo(replyId, changeDto);
        Reply reply = replyService.findOne(replyId).orElseThrow();

        //then
        assertThat(reply.getContent()).isEqualTo("new reply");
        assertThat(reply.getWriteTime()).isNotEqualTo(firstWriteTime);
    }

    @Test
    @DisplayName("댓글을 삭제할 수 있다.")
    void deleteAnimal() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        memberService.join(member);

        Long boardId = boardService.write(member.getId(), new WriteBoardDto());

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("hi");

        Long replyId = replyService.reply(member.getId(), boardId, replyDto);

        //when
        replyService.deleteReply(replyId);

        Board board = boardService.findOne(boardId).orElseThrow();

        //then
        assertThat(replyService.findOne(replyId).isPresent()).isFalse();
        assertThat(replyService.findAll().size()).isEqualTo(0);
        assertThat(member.getReplies().isEmpty()).isTrue();
        assertThat(board.getReplies().isEmpty()).isTrue();
    }
}
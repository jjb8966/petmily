package com.petmily.service;

import com.petmily.builder.BoardBuilder;
import com.petmily.builder.MemberBuilder;
import com.petmily.domain.Board;
import com.petmily.domain.Member;
import com.petmily.domain.Reply;
import com.petmily.dto.reply.ChangeReplyDto;
import com.petmily.dto.reply.ReplyDto;
import com.petmily.enum_type.BoardType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
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
        Board board = new BoardBuilder(member, BoardType.FREE).setTitle("BoardA").build();
        memberService.join(member);
        boardService.write(board);

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("hi");

        //when
        Long replyId = replyService.reply(member.getId(), board.getId(), replyDto);
        Reply reply = replyService.findOne(replyId).orElseThrow();

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
        Board board = new BoardBuilder(memberA, BoardType.FREE).setTitle("BoardA").build();
        memberService.join(memberA);
        memberService.join(memberB);
        boardService.write(board);

        ReplyDto firstReply = new ReplyDto();
        ReplyDto secondReply = new ReplyDto();
        ReplyDto thirdReply = new ReplyDto();
        firstReply.setContent("first memberB");
        secondReply.setContent("second memberA");
        thirdReply.setContent("third memberB");

        //when
        replyService.reply(memberB.getId(), board.getId(), firstReply);
        replyService.reply(memberA.getId(), board.getId(), secondReply);
        replyService.reply(memberB.getId(), board.getId(), thirdReply);

        List<Reply> allReplies = replyService.findAll();

        for (Reply reply : allReplies) {
            log.info("reply = {}", reply);
        }

        //then
        assertThat(allReplies.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("댓글 내용을 변경할 수 있다.")
    void changeReplyInfo() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        Board board = new BoardBuilder(member, BoardType.FREE).setTitle("BoardA").build();
        memberService.join(member);
        boardService.write(board);

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("old reply");

        Long replyId = replyService.reply(member.getId(), board.getId(), replyDto);
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
        Board board = new BoardBuilder(member, BoardType.FREE).setTitle("BoardA").build();
        memberService.join(member);
        boardService.write(board);

        ReplyDto replyDto = new ReplyDto();
        replyDto.setContent("hi");
        Long replyId = replyService.reply(member.getId(), board.getId(), replyDto);

        //when
        replyService.deleteReply(replyId);

        boolean isPresent = replyService.findOne(replyId).isPresent();
        List<Reply> all = replyService.findAll();

        //then
        assertThat(isPresent).isFalse();
        assertThat(all.size()).isEqualTo(0);
    }
}
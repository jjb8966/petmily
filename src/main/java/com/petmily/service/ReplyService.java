package com.petmily.service;

import com.petmily.domain.builder.ReplyBuilder;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import com.petmily.domain.dto.reply.WriteReplyForm;
import com.petmily.repository.BoardRepository;
import com.petmily.repository.MemberRepository;
import com.petmily.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final MessageSource ms;

    // 댓글 작성
    @Transactional
    public Long reply(Long memberId, Long boardId, WriteReplyForm writeReplyForm) {
        Member member = getMember(memberId);
        Board board = getBoard(boardId);

        Reply reply = new ReplyBuilder(member, board)
                .setContent(writeReplyForm.getContent())
                .build();

        replyRepository.save(reply);

        return reply.getId();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.member.null")));
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.board.null")));
    }

//    // 댓글 수정
//    @Transactional
//    public Long changeReplyInfo(Long id, ChangeReplyForm replyDto) {
//        Reply reply = replyRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.reply.null")));
//
//        reply.changeInfo(replyDto);
//
//        return reply.getId();
//    }

    // 댓글 삭제
    @Transactional
    public void deleteReply(Long id) {
        replyRepository.deleteById(id);
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}

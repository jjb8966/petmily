package com.petmily.service;

import com.petmily.domain.builder.ReplyBuilder;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import com.petmily.domain.dto.reply.ChangeReplyDto;
import com.petmily.domain.dto.reply.WriteReplyForm;
import com.petmily.repository.BoardRepository;
import com.petmily.repository.MemberRepository;
import com.petmily.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    // 댓글 작성
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    private Board getBoard(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    // 댓글 조회
    public Optional<Reply> findOne(Long id) {
        return replyRepository.findById(id);
    }

    // 전체 댓글 조회
    public List<Reply> findAll() {
        return replyRepository.findAll();
    }

    // 댓글 수정
    public Long changeReplyInfo(Long id, ChangeReplyDto replyDto) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        reply.changeInfo(replyDto);

        return reply.getId();
    }

    // 댓글 삭제
    public void deleteReply(Long id) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        reply.getMember()
                .getReplies()
                .removeIf(r -> r.getId().equals(id));

        reply.getBoard()
                .getReplies()
                .removeIf(r -> r.getId().equals(id));

        replyRepository.deleteById(id);
    }

}

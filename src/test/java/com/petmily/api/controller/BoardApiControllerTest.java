package com.petmily.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.ReplyBuilder;
import com.petmily.domain.builder.board.BoardBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.dto.board.ModifyBoardApiForm;
import com.petmily.domain.enum_type.BoardType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class BoardApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager em;

    @Autowired
    ObjectMapper objectMapper;

    Member member;

    @BeforeEach
    void before() {
        member = new MemberBuilder("member", "123")
                .setName("memberA")
                .build();

        em.persist(member);
    }

    @Test
    @DisplayName("모든 게시글을 조회할 수 있다.")
    void getList() throws Exception {
        // given
        Board board1 = new BoardBuilder(member, BoardType.FREE)
                .setTitle("title1")
                .setContent("content1")
                .setShownAll(true)
                .build();

        Board board2 = new BoardBuilder(member, BoardType.INQUIRY)
                .setTitle("title2")
                .setContent("content2")
                .setShownAll(false)
                .build();

        em.persist(board1);
        em.persist(board2);

        // when, then
        mockMvc.perform(get("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.data[0].boardType").value(board1.getBoardType().name()))
                .andExpect(jsonPath("$.data[0].title").value(board1.getTitle()))
                .andExpect(jsonPath("$.data[0].shownAll").value(board1.getShownAll()))
                .andExpect(jsonPath("$.data[0].writerName").value(board1.getMember().getName()))
                .andExpect(jsonPath("$.data[1].boardType").value(board2.getBoardType().name()))
                .andExpect(jsonPath("$.data[1].title").value(board2.getTitle()))
                .andExpect(jsonPath("$.data[1].shownAll").value(board2.getShownAll()))
                .andExpect(jsonPath("$.data[1].writerName").value(board2.getMember().getName()))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글의 상세정보를 조회할 수 있다.")
    void boardDetail() throws Exception {
        // given
        Board board = new BoardBuilder(member, BoardType.FREE)
                .setTitle("title")
                .setContent("content")
                .setShownAll(true)
                .build();

        em.persist(board);

        // when, then
        mockMvc.perform(get("/api/boards/{boardId}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(board.getId()))
                .andExpect(jsonPath("$.writerName").value(board.getMember().getName()))
                .andExpect(jsonPath("$.boardType").value(board.getBoardType().name()))
                .andExpect(jsonPath("$.title").value(board.getTitle()))
                .andExpect(jsonPath("$.content").value(board.getContent()))
                .andExpect(jsonPath("$.shownAll").value(board.getShownAll()))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글을 삭제할 수 있다.")
    void deleteBoard() throws Exception {
        // given
        Board board = new BoardBuilder(member, BoardType.FREE)
                .setTitle("title")
                .setContent("content")
                .setShownAll(true)
                .build();

        em.persist(board);

        // when, then
        mockMvc.perform(delete("/api/boards/{boardId}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시글이 삭제되었습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글의 댓글을 삭제할 수 있다.")
    void deleteReply() throws Exception {
        // given
        Board board = new BoardBuilder(member, BoardType.FREE)
                .setTitle("title")
                .setContent("content")
                .setShownAll(true)
                .build();

        Reply reply = new ReplyBuilder(member, board)
                .setContent("reply").build();

        em.persist(board);
        em.persist(reply);

        // when, then
        mockMvc.perform(delete("/api/boards/replies/{replyId}", reply.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("댓글이 삭제되었습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글의 정보를 수정할 수 있다.")
    void modifyAnimal() throws Exception {
        // given
        Board board = new BoardBuilder(member, BoardType.FREE)
                .setTitle("title")
                .setContent("content")
                .setShownAll(true)
                .build();

        em.persist(board);

        ModifyBoardApiForm form = new ModifyBoardApiForm();
        form.setBoardType("INQUIRY");
        form.setTitle("new title");
        form.setContent("new content");
        form.setShownAll(false);

        // when, then
        mockMvc.perform(patch("/api/boards/{boardId}", board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시글 정보가 변경되었습니다."))
                .andDo(print());

    }

}
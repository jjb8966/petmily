package com.petmily.api;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.enum_type.MemberGrade;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class MemberApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("모든 회원을 조회할 수 있다.")
    void getList() throws Exception {
        // given
        Member member1 = new MemberBuilder("member1", "123")
                .setName("memberA")
                .build();

        Member member2 = new MemberBuilder("member2", "123")
                .setName("memberB")
                .build();

        em.persist(member1);
        em.persist(member2);

        // when, then
        mockMvc.perform(get("/api/members")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.data[0].grade").value(MemberGrade.NORMAL.name()))
                .andExpect(jsonPath("$.data[0].loginId").value(member1.getLoginId()))
                .andExpect(jsonPath("$.data[0].name").value(member1.getName()))
                .andExpect(jsonPath("$.data[1].grade").value(MemberGrade.NORMAL.name()))
                .andExpect(jsonPath("$.data[1].loginId").value(member2.getLoginId()))
                .andExpect(jsonPath("$.data[1].name").value(member2.getName()))
                .andDo(print());
    }

    @Test
    @DisplayName("회원을 삭제할 수 있다.")
    void withdrawMember() throws Exception {
        // given
        Member member1 = new MemberBuilder("member1", "123")
                .setName("memberA")
                .build();

        em.persist(member1);

        // when, then
        mockMvc.perform(delete("/api/members/" + member1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원이 탈퇴되었습니다."))
                .andDo(print());
    }
}
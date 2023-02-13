package com.petmily.api.controller;

import com.petmily.config.formatter.AccountNumberFormatter;
import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.application.DonationBuilder;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BankType;
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
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class DonationApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountNumberFormatter accountNumberFormatter;

    @Autowired
    EntityManager em;

    Member member;
    AbandonedAnimal animal1;
    AbandonedAnimal animal2;

    @BeforeEach
    void before() {
        member = new MemberBuilder("memberA", "123").build();

        animal1 = new AbandonedAnimalBuilder()
                .setName("animalA")
                .setSpecies(AnimalSpecies.CAT)
                .build();

        animal2 = new AbandonedAnimalBuilder()
                .setName("animalB")
                .setSpecies(AnimalSpecies.DOG)
                .build();

        em.persist(member);
        em.persist(animal1);
        em.persist(animal2);
    }

    @Test
    @DisplayName("모든 후원을 조회할 수 있다.")
    void getList() throws Exception {
        // given
        Donation donation1 = new DonationBuilder(member, animal1)
                .setBacker(member.getName())
                .setBankType(BankType.KB)
                .setAccountNumber(accountNumberFormatter.parse("1111-2222-3333-4444", Locale.KOREA))
                .build();

        Donation donation2 = new DonationBuilder(member, animal1)
                .setBacker(member.getName())
                .setBankType(BankType.KB)
                .setAccountNumber(accountNumberFormatter.parse("1111-2222-3333-4444", Locale.KOREA))
                .build();

        em.persist(donation1);
        em.persist(donation2);

        // when, then
        mockMvc.perform(get("/api/donations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.data[0].type").value(donation1.getApplicationType()))
                .andExpect(jsonPath("$.data[0].animalName").value(donation1.getAbandonedAnimal().getName()))
                .andExpect(jsonPath("$.data[0].status").value(donation1.getApplicationStatus().name()))
                .andExpect(jsonPath("$.data[0].backer").value(donation1.getBacker()))
                .andExpect(jsonPath("$.data[1].type").value(donation2.getApplicationType()))
                .andExpect(jsonPath("$.data[1].animalName").value(donation2.getAbandonedAnimal().getName()))
                .andExpect(jsonPath("$.data[1].status").value(donation2.getApplicationStatus().name()))
                .andExpect(jsonPath("$.data[1].backer").value(donation2.getBacker()))
                .andDo(print());
    }

}
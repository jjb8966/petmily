package com.petmily.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.builder.PictureBuilder;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.dto.abandoned_animal.ModifyAnimalApiForm;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
class AnimalApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager em;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("모든 유기동물을 조회할 수 있다.")
    void getList() throws Exception {
        // given
        AbandonedAnimal animal1 = new AbandonedAnimalBuilder()
                .setName("dog1")
                .setSpecies(AnimalSpecies.DOG)
                .setStatus(AnimalStatus.ADOPTED)
                .build();

        AbandonedAnimal animal2 = new AbandonedAnimalBuilder()
                .setName("dog2")
                .setSpecies(AnimalSpecies.DOG)
                .setStatus(AnimalStatus.TEMP_PROTECTED)
                .build();

        em.persist(animal1);
        em.persist(animal2);

        // when, then
        mockMvc.perform(get("/api/animals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.data[0].name").value(animal1.getName()))
                .andExpect(jsonPath("$.data[0].species").value(animal1.getSpecies().name()))
                .andExpect(jsonPath("$.data[0].status").value(animal1.getStatus().name()))
                .andExpect(jsonPath("$.data[1].name").value(animal2.getName()))
                .andExpect(jsonPath("$.data[1].species").value(animal2.getSpecies().name()))
                .andExpect(jsonPath("$.data[1].status").value(animal2.getStatus().name()))
                .andDo(print());
    }

    @Test
    @DisplayName("유기동물의 상세정보를 조회할 수 있다.")
    void animalDetail() throws Exception {
        // given
        AbandonedAnimal animal = new AbandonedAnimalBuilder()
                .setPicture(new PictureBuilder()
                        .setFileStoreName("picture1.jpg")
                        .build())
                .setSpecies(AnimalSpecies.DOG)
                .setStatus(AnimalStatus.ADOPTED)
                .setName("dog")
                .setKind("진돗개")
                .setAge(10)
                .setWeight(1.0F)
                .build();

        em.persist(animal);

        // when, then
        mockMvc.perform(get("/api/animals/{animalId}", animal.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(animal.getId()))
                .andExpect(jsonPath("$.pictureStoreName").value(animal.getPicture().getFileStoreName()))
                .andExpect(jsonPath("$.species").value(animal.getSpecies().name()))
                .andExpect(jsonPath("$.status").value(animal.getStatus().name()))
                .andExpect(jsonPath("$.name").value(animal.getName()))
                .andExpect(jsonPath("$.kind").value(animal.getKind()))
                .andExpect(jsonPath("$.age").value(animal.getAge()))
                .andExpect(jsonPath("$.weight").value(animal.getWeight()))
                .andDo(print());
    }

    @Test
    @DisplayName("유기동물을 삭제할 수 있다.")
    void deleteAnimal() throws Exception {
        // given
        AbandonedAnimal animal = new AbandonedAnimalBuilder()
                .setPicture(new PictureBuilder()
                        .setFileStoreName("picture1.jpg")
                        .build())
                .setSpecies(AnimalSpecies.DOG)
                .setStatus(AnimalStatus.ADOPTED)
                .setName("dog")
                .setKind("진돗개")
                .setAge(10)
                .setWeight(1.0F)
                .build();

        em.persist(animal);

        // when, then
        mockMvc.perform(delete("/api/animals/{animalId}", animal.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("유기동물이 삭제되었습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("유기동물의 정보를 수정할 수 있다.")
    void modifyAnimal() throws Exception {
        // given
        AbandonedAnimal animal = new AbandonedAnimalBuilder()
                .setPicture(new PictureBuilder()
                        .setFileStoreName("picture1.jpg")
                        .build())
                .setSpecies(AnimalSpecies.DOG)
                .setStatus(AnimalStatus.ADOPTED)
                .setName("dog")
                .setKind("진돗개")
                .setAge(10)
                .setWeight(1.0F)
                .build();

        em.persist(animal);

        ModifyAnimalApiForm form = new ModifyAnimalApiForm();
        form.setSpecies("CAT");
        form.setStatus("PROTECTED");
        form.setName("cat");
        form.setKind("페르시안");
        form.setAge(3);
        form.setWeight(2.0F);

        // when, then
        mockMvc.perform(patch("/api/animals/{animalId}", animal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(form)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("유기동물의 정보가 변경되었습니다."))
                .andDo(print());

    }

}
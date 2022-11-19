//package com.petmily.repository;
//
//import com.petmily.domain.core.AbandonedAnimal;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class AbandonedAnimalRepositoryTest {
//
//    @Autowired
//    AbandonedAnimalRepository abandonedAnimalRepository;
//
//    @Test
//    void paging() {
//        PageRequest pageRequest = PageRequest.of(0, 5);
//
//        Page<AbandonedAnimal> page = abandonedAnimalRepository.findAll(pageRequest);
//
//        System.out.println("page.getTotalPages() = " + page.getTotalPages());
//        List<AbandonedAnimal> content = page.getContent();
//        for (AbandonedAnimal animal : content) {
//            System.out.println("animal = " + animal);
//        }
//    }
//}
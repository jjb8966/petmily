package com.petmily.repository;

import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.application.AdoptBuilder;
import com.petmily.domain.builder.application.DonationBuilder;
import com.petmily.domain.builder.application.TemporaryProtectionBuilder;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.core.application.TemporaryProtection;
import com.petmily.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class ApplicationRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberService memberService;

    @Autowired
    ApplicationRepository applicationRepository;

    @Test
    void findAllByMember() {
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        em.persist(member);
        em.persist(animal);

        Donation donation = new DonationBuilder(member, animal).build();
        Adopt adopt = new AdoptBuilder(member, animal).build();
        TemporaryProtection temporaryProtection = new TemporaryProtectionBuilder(member, animal).build();

        member.getApplications().add(donation);
        member.getApplications().add(adopt);
        member.getApplications().add(temporaryProtection);

        List<Application> allByMember = applicationRepository.findAllByMemberOrderByApplicationType(member);

        em.flush();
        em.clear();

        assertThat(allByMember.size()).isEqualTo(3);
        assertThat(allByMember).containsExactly(donation, adopt, temporaryProtection);
    }


    @Test
    void getDiscriminatorColumn() {
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        em.persist(member);
        em.persist(animal);

        Donation donation = new DonationBuilder(member, animal).build();
        Adopt adopt = new AdoptBuilder(member, animal).build();
        TemporaryProtection temporaryProtection = new TemporaryProtectionBuilder(member, animal).build();

        em.flush();
        em.clear();

        Application findDonation = em.find(Application.class, donation.getId());
        Application findAdopt = em.find(Application.class, adopt.getId());
        Application findTempProtection = em.find(Application.class, temporaryProtection.getId());

        assertThat(findDonation.getApplicationType()).isEqualTo("Donation");
        assertThat(findAdopt.getApplicationType()).isEqualTo("Adopt");
        assertThat(findTempProtection.getApplicationType()).isEqualTo("TemporaryProtection");
    }
}
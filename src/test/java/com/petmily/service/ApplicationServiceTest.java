package com.petmily.service;

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
import com.petmily.domain.core.embeded_type.AccountNumber;
import com.petmily.domain.core.enum_type.BankType;
import com.petmily.domain.core.enum_type.LocationType;
import com.petmily.domain.dto.application.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class ApplicationServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberService memberService;

    @Autowired
    AbandonedAnimalService animalService;

    @Autowired
    ApplicationService applicationService;

    @Test
    @DisplayName("회원이 유기동물 후원 신청을 할 수 있다.")
    void donate() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        em.persist(member);
        em.persist(animal);

        ApplyDonationForm applyDonationForm = new ApplyDonationForm();
        applyDonationForm.setBankType(BankType.KB);
        applyDonationForm.setDonator("memberA");
        applyDonationForm.setAccountNumber(new AccountNumber("111-2222-3333"));
        applyDonationForm.setAmount(3000);

        //when
        Long donationId = applicationService.donate(member.getId(), animal.getId(), applyDonationForm);
        Donation findDonation = em.find(Donation.class, donationId);

        //then
        assertThat(findDonation.getMember()).isEqualTo(member);
        assertThat(findDonation.getAbandonedAnimal()).isEqualTo(animal);
        assertThat(findDonation.getBankType()).isEqualTo(applyDonationForm.getBankType());
        assertThat(findDonation.getDonator()).isEqualTo(applyDonationForm.getDonator());
        assertThat(findDonation.getAccountNumber()).isEqualTo(applyDonationForm.getAccountNumber());
        assertThat(findDonation.getAmount()).isEqualTo(applyDonationForm.getAmount());
    }

    @Test
    @DisplayName("회원이 유기동물 임시보호 신청을 할 수 있다.")
    void tempProtect() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        em.persist(member);
        em.persist(animal);

        ApplyTempProtectionForm applyTempProtectionForm = new ApplyTempProtectionForm();
        applyTempProtectionForm.setLocation(LocationType.SEOUL);
        applyTempProtectionForm.setMarried(true);
        applyTempProtectionForm.setJob("학생");
        applyTempProtectionForm.setPeriod(10);

        //when
        Long tempProtectionId = applicationService.tempProtect(member.getId(), animal.getId(), applyTempProtectionForm);
        TemporaryProtection findTempProtection = em.find(TemporaryProtection.class, tempProtectionId);

        //then
        assertThat(findTempProtection.getMember()).isEqualTo(member);
        assertThat(findTempProtection.getAbandonedAnimal()).isEqualTo(animal);
        assertThat(findTempProtection.getLocation()).isEqualTo(applyTempProtectionForm.getLocation());
        assertThat(findTempProtection.getMarried()).isEqualTo(applyTempProtectionForm.getMarried());
        assertThat(findTempProtection.getJob()).isEqualTo(applyTempProtectionForm.getJob());
    }

    @Test
    @DisplayName("회원이 유기동물 입양 신청을 할 수 있다.")
    void adopt() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        em.persist(member);
        em.persist(animal);

        ApplyAdoptForm applyAdoptForm = new ApplyAdoptForm();
        applyAdoptForm.setLocation(LocationType.SEOUL);
        applyAdoptForm.setMarried(true);
        applyAdoptForm.setJob("학생");

        //when
        Long adoptId = applicationService.adopt(member.getId(), animal.getId(), applyAdoptForm);
        Adopt findAdopt = em.find(Adopt.class, adoptId);

        //then
        assertThat(findAdopt.getMember()).isEqualTo(member);
        assertThat(findAdopt.getAbandonedAnimal()).isEqualTo(animal);
        assertThat(findAdopt.getLocation()).isEqualTo(applyAdoptForm.getLocation());
        assertThat(findAdopt.getMarried()).isEqualTo(applyAdoptForm.getMarried());
        assertThat(findAdopt.getJob()).isEqualTo(applyAdoptForm.getJob());
    }

    @Test
    @DisplayName("지원서를 종류에 맞게 조회할 수 있다.")
    void findOne() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        Donation donation = new DonationBuilder(member, animal).build();
        TemporaryProtection temporaryProtection = new TemporaryProtectionBuilder(member, animal).build();
        Adopt adopt = new AdoptBuilder(member, animal).build();

        em.persist(member);

        //when
        Application findDonation = applicationService.findOne(donation.getId(), Donation.class).orElseThrow();
        Application findTempProtection = applicationService.findOne(temporaryProtection.getId(), TemporaryProtection.class).orElseThrow();
        Application findAdopt = applicationService.findOne(adopt.getId(), Adopt.class).orElseThrow();

        //then
        assertThat(findDonation).isInstanceOf(Donation.class);
        assertThat(findTempProtection).isInstanceOf(TemporaryProtection.class);
        assertThat(findAdopt).isInstanceOf(Adopt.class);
    }

    @Test
    @DisplayName("전체 지원서를 조회할 수 있다.")
    void findAll() {
        //given
        Member memberA = new MemberBuilder("memberA", "123").build();
        Member memberB = new MemberBuilder("memberB", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        Donation donation = new DonationBuilder(memberA, animal).build();
        TemporaryProtection temporaryProtection = new TemporaryProtectionBuilder(memberA, animal).build();
        Adopt adoptA = new AdoptBuilder(memberA, animal).build();
        Adopt adoptB = new AdoptBuilder(memberB, animal).build();

        em.persist(memberA);
        em.persist(memberB);

        //when
        List<Application> allApplication = applicationService.findAll();

        //then
        assertThat(allApplication).hasSize(4);
        assertThat(allApplication).contains(donation, temporaryProtection, adoptA, adoptB);
    }

    @Test
    @DisplayName("특정 회원의 전체 지원서를 조회할 수 있다.")
    void findAll_about_member() {
        //given
        Member memberA = new MemberBuilder("memberA", "123").build();
        Member memberB = new MemberBuilder("memberB", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        Donation donation = new DonationBuilder(memberA, animal).build();
        TemporaryProtection temporaryProtection = new TemporaryProtectionBuilder(memberA, animal).build();
        Adopt adoptA = new AdoptBuilder(memberA, animal).build();
        Adopt adoptB = new AdoptBuilder(memberB, animal).build();

        em.persist(memberA);
        em.persist(memberB);

        //when
        List<Application> allApplicationAboutMemberA = applicationService.findAll(memberA);

        //then
        assertThat(allApplicationAboutMemberA).hasSize(3);
        assertThat(allApplicationAboutMemberA).contains(donation, temporaryProtection, adoptA);
    }

    @Test
    @DisplayName("후원 신청서 정보를 변경할 수 있다.")
    void modifyDonation() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        em.persist(member);
        em.persist(animal);

        ApplyDonationForm applyDonationForm = new ApplyDonationForm();
        applyDonationForm.setBankType(BankType.KB);
        applyDonationForm.setDonator("memberA");
        applyDonationForm.setAccountNumber(new AccountNumber("111-2222-3333"));
        applyDonationForm.setAmount(3000);

        Long donationId = applicationService.donate(member.getId(), animal.getId(), applyDonationForm);

        ModifyDonationForm modifyDonationForm = new ModifyDonationForm();
        modifyDonationForm.setBankType(BankType.NH);
        modifyDonationForm.setDonator("memberB");
        modifyDonationForm.setAccountNumber(new AccountNumber("123-4567-89"));
        modifyDonationForm.setAmount(5000);

        //when
        applicationService.modifyDonation(donationId, modifyDonationForm);
        Donation findDonation = em.find(Donation.class, donationId);

        //then
        assertThat(findDonation.getBankType()).isEqualTo(modifyDonationForm.getBankType());
        assertThat(findDonation.getDonator()).isEqualTo(modifyDonationForm.getDonator());
        assertThat(findDonation.getAccountNumber()).isEqualTo(modifyDonationForm.getAccountNumber());
        assertThat(findDonation.getAmount()).isEqualTo(modifyDonationForm.getAmount());
    }

    @Test
    @DisplayName("임시보호 신청서 정보를 변경할 수 있다.")
    void changeTempProtectionInfo() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        em.persist(member);
        em.persist(animal);

        ApplyTempProtectionForm applyTempProtectionForm = new ApplyTempProtectionForm();
        applyTempProtectionForm.setLocation(LocationType.SEOUL);
        applyTempProtectionForm.setMarried(true);
        applyTempProtectionForm.setJob("학생");
        applyTempProtectionForm.setPeriod(10);

        Long tempProtectionId = applicationService.tempProtect(member.getId(), animal.getId(), applyTempProtectionForm);

        ModifyTempProtectionForm modifyTempProtectionForm = new ModifyTempProtectionForm();
        modifyTempProtectionForm.setLocation(LocationType.CHUNGCHEONG);
        modifyTempProtectionForm.setMarried(false);
        modifyTempProtectionForm.setJob("의사");
        modifyTempProtectionForm.setPeriod(5);

        //when
        applicationService.modifyTempProtection(tempProtectionId, modifyTempProtectionForm);
        TemporaryProtection findTempProtection = em.find(TemporaryProtection.class, tempProtectionId);

        //then
        assertThat(findTempProtection.getLocation()).isEqualTo(modifyTempProtectionForm.getLocation());
        assertThat(findTempProtection.getMarried()).isEqualTo(modifyTempProtectionForm.getMarried());
        assertThat(findTempProtection.getJob()).isEqualTo(modifyTempProtectionForm.getJob());
        assertThat(findTempProtection.getPeriod()).isEqualTo(modifyTempProtectionForm.getPeriod());
    }

    @Test
    @DisplayName("입양 신청서 정보를 변경할 수 있다.")
    void changeAdoptInfo() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        em.persist(member);
        em.persist(animal);

        ApplyAdoptForm applyAdoptForm = new ApplyAdoptForm();
        applyAdoptForm.setLocation(LocationType.SEOUL);
        applyAdoptForm.setMarried(true);
        applyAdoptForm.setJob("학생");

        Long adoptId = applicationService.adopt(member.getId(), animal.getId(), applyAdoptForm);

        ModifyAdoptForm modifyAdoptForm = new ModifyAdoptForm();
        modifyAdoptForm.setLocation(LocationType.GANGWON);
        modifyAdoptForm.setMarried(false);
        modifyAdoptForm.setJob("개발자");

        //when
        applicationService.modifyAdopt(adoptId, modifyAdoptForm);
        Adopt findAdopt = em.find(Adopt.class, adoptId);

        //then
        assertThat(findAdopt.getLocation()).isEqualTo(modifyAdoptForm.getLocation());
        assertThat(findAdopt.getMarried()).isEqualTo(modifyAdoptForm.getMarried());
        assertThat(findAdopt.getJob()).isEqualTo(modifyAdoptForm.getJob());
    }

    @Test
    @DisplayName("지원서를 삭제할 수 있다.")
    void delete() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();

        em.persist(member);
        em.persist(animal);

        Long adoptId = applicationService.adopt(member.getId(), animal.getId(), new ApplyAdoptForm());

        //when
        applicationService.deleteApplication(adoptId);
        Adopt findAdopt = em.find(Adopt.class, adoptId);

        //then
        assertThat(findAdopt).isNull();
    }
}
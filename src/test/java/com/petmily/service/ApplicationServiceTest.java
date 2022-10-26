package com.petmily.service;

import com.petmily.builder.AbandonedAnimalBuilder;
import com.petmily.builder.MemberBuilder;
import com.petmily.domain.AbandonedAnimal;
import com.petmily.domain.Member;
import com.petmily.domain.application.Adopt;
import com.petmily.domain.application.Application;
import com.petmily.domain.application.Donation;
import com.petmily.domain.application.TemporaryProtection;
import com.petmily.dto.application.*;
import com.petmily.enum_type.ApplicationStatus;
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
    @DisplayName("회원이 유기동물 입양 신청을 할 수 있다.")
    void adopt() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        memberService.join(member);
        animalService.register(animal);

        ApplyAdoptDto adoptDto = new ApplyAdoptDto();
        adoptDto.setAddress("addressA");
        adoptDto.setJob("chef");
        adoptDto.setMarried('N');

        //when
        Long adoptId = applicationService.adopt(member.getId(), animal.getId(), adoptDto);
        Adopt adopt = applicationService.findOne(adoptId, Adopt.class).orElseThrow();

        log.info("adopt = {}", adopt);

        //then
        assertThat(adopt.getMember().getLoginId()).isEqualTo("memberA");
        assertThat(adopt.getAbandonedAnimal().getName()).isEqualTo("animalA");
        assertThat(adopt.getAddress()).isEqualTo("addressA");
        assertThat(adopt.getApplicationStatus()).isEqualTo(ApplicationStatus.WAIT);
        assertThat(member.getApplications().contains(adopt)).isTrue();
        assertThat(animal.getApplications().contains(adopt)).isTrue();
    }

    @Test
    @DisplayName("회원이 유기동물 임시보호 신청을 할 수 있다.")
    void tempProtect() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        memberService.join(member);
        animalService.register(animal);

        ApplyTempProtectionDto tempProtectionDto = new ApplyTempProtectionDto();
        tempProtectionDto.setPeriod(10);

        //when
        Long tempProtectId = applicationService.tempProtect(member.getId(), animal.getId(), tempProtectionDto);
        TemporaryProtection temporaryProtection = applicationService.findOne(tempProtectId, TemporaryProtection.class).orElseThrow();

        log.info("temporaryProtection = {}", temporaryProtection);

        //then
        assertThat(temporaryProtection.getMember().getLoginId()).isEqualTo("memberA");
        assertThat(temporaryProtection.getAbandonedAnimal().getName()).isEqualTo("animalA");
        assertThat(temporaryProtection.getPeriod()).isEqualTo(10);
        assertThat(temporaryProtection.getApplicationStatus()).isEqualTo(ApplicationStatus.WAIT);
        assertThat(member.getApplications().contains(temporaryProtection)).isTrue();
        assertThat(animal.getApplications().contains(temporaryProtection)).isTrue();
    }

    @Test
    @DisplayName("회원이 유기동물 후원 신청을 할 수 있다.")
    void donate() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        memberService.join(member);
        animalService.register(animal);

        ApplyDonationDto donationDto = new ApplyDonationDto();
        donationDto.setAmount(100_000);

        //when
        Long donateId = applicationService.donate(member.getId(), animal.getId(), donationDto);
        Donation donation = applicationService.findOne(donateId, Donation.class).orElseThrow();

        log.info("donation = {}", donateId);

        //then
        assertThat(donation.getMember().getLoginId()).isEqualTo("memberA");
        assertThat(donation.getAbandonedAnimal().getName()).isEqualTo("animalA");
        assertThat(donation.getAmount()).isEqualTo(100000);
        assertThat(donation.getApplicationStatus()).isEqualTo(ApplicationStatus.WAIT);
        assertThat(member.getApplications().contains(donation)).isTrue();
        assertThat(animal.getApplications().contains(donation)).isTrue();
    }

    @Test
    void delete() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        memberService.join(member);
        animalService.register(animal);

        ApplyDonationDto donationDto = new ApplyDonationDto();
        donationDto.setAmount(100_000);
        Long donateId = applicationService.donate(member.getId(), animal.getId(), donationDto);

        //when
        applicationService.deleteApplication(donateId);

        boolean isPresent = applicationService.findOne(donateId, Adopt.class).isPresent();
        List<Application> all = applicationService.findAll();

        //then
        assertThat(isPresent).isFalse();
        assertThat(all.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("입양 신청서 정보를 변경할 수 있다.")
    void changeAdoptInfo() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        memberService.join(member);
        animalService.register(animal);

        ApplyAdoptDto adoptDto = new ApplyAdoptDto();
        adoptDto.setAddress("addressA");
        adoptDto.setJob("chef");
        adoptDto.setMarried('N');

        Long adoptId = applicationService.adopt(member.getId(), animal.getId(), adoptDto);

        ChangeAdoptDto changeAdoptDto = new ChangeAdoptDto();
        changeAdoptDto.setAddress("addressB");
        changeAdoptDto.setJob("teacher");
        changeAdoptDto.setMarried('Y');

        //when
        applicationService.changeAdoptInfo(adoptId, changeAdoptDto);

        Adopt adopt = applicationService.findOne(adoptId, Adopt.class).orElseThrow();

        //then
        assertThat(adopt.getAddress()).isEqualTo(changeAdoptDto.getAddress());
        assertThat(adopt.getJob()).isEqualTo(changeAdoptDto.getJob());
        assertThat(adopt.getMarried()).isEqualTo(changeAdoptDto.getMarried());
        assertThat(adopt.getMember()).isEqualTo(member);
        assertThat(adopt.getAbandonedAnimal()).isEqualTo(animal);
        assertThat(member.getApplications().contains(adopt)).isTrue();
        assertThat(animal.getApplications().contains(adopt)).isTrue();
    }

    @Test
    @DisplayName("임시보호 신청서 정보를 변경할 수 있다.")
    void changeTempProtectionInfo() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        memberService.join(member);
        animalService.register(animal);

        ApplyTempProtectionDto tempProtectionDto = new ApplyTempProtectionDto();
        tempProtectionDto.setPeriod(10);

        Long tempProtectId = applicationService.tempProtect(member.getId(), animal.getId(), tempProtectionDto);

        ChangeTempProtectionDto changeTempProtectionDto = new ChangeTempProtectionDto();
        changeTempProtectionDto.setPeriod(100);

        //when
        applicationService.changeTempProtectionInfo(tempProtectId, changeTempProtectionDto);

        TemporaryProtection temporaryProtection = applicationService.findOne(tempProtectId, TemporaryProtection.class).orElseThrow();

        //then
        assertThat(temporaryProtection.getPeriod()).isEqualTo(changeTempProtectionDto.getPeriod());
        assertThat(temporaryProtection.getMember()).isEqualTo(member);
        assertThat(temporaryProtection.getAbandonedAnimal()).isEqualTo(animal);
        assertThat(member.getApplications().contains(temporaryProtection)).isTrue();
        assertThat(animal.getApplications().contains(temporaryProtection)).isTrue();
    }

    @Test
    @DisplayName("후원 신청서 정보를 변경할 수 있다.")
    void changeDonationInfo() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        memberService.join(member);
        animalService.register(animal);

        ApplyDonationDto donationDto = new ApplyDonationDto();
        donationDto.setAmount(1000);

        Long donateId = applicationService.donate(member.getId(), animal.getId(), donationDto);

        ChangeDonationDto changeDonationDto = new ChangeDonationDto();
        changeDonationDto.setAmount(10000000);

        //when
        applicationService.changeDonationInfo(donateId, changeDonationDto);
        Donation donation = applicationService.findOne(donateId, Donation.class).orElseThrow();

        //then
        assertThat(donation.getAmount()).isEqualTo(changeDonationDto.getAmount());
        assertThat(donation.getMember()).isEqualTo(member);
        assertThat(donation.getAbandonedAnimal()).isEqualTo(animal);
        assertThat(member.getApplications().contains(donation)).isTrue();
        assertThat(animal.getApplications().contains(donation)).isTrue();
    }
}
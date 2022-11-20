package com.petmily.service;

import com.petmily.domain.builder.application.AdoptBuilder;
import com.petmily.domain.builder.application.DonationBuilder;
import com.petmily.domain.builder.application.TemporaryProtectionBuilder;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.core.application.TemporaryProtection;
import com.petmily.domain.dto.application.*;
import com.petmily.repository.AbandonedAnimalRepository;
import com.petmily.repository.ApplicationRepository;
import com.petmily.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final AbandonedAnimalRepository animalRepository;

    // 입양하기
    @Transactional
    public Long adopt(Long memberId, Long animalId, ApplyAdoptForm adoptDto) {
        // 엔티티 조회
        Member member = getMember(memberId);
        AbandonedAnimal animal = getAnimal(animalId);

        // 지원서 생성
        Adopt adopt = new AdoptBuilder(member, animal)
                .setLocation(adoptDto.getLocation())
                .setJob(adoptDto.getJob())
                .setMarried(adoptDto.getMarried())
                .build();

        // 지원서 저장
        applicationRepository.save(adopt);

        return adopt.getId();
    }

    // 임시보호하기
    @Transactional
    public Long tempProtect(Long memberId, Long animalId, ApplyTempProtectionForm tempProtectionDto) {
        // 엔티티 조회
        Member member = getMember(memberId);
        AbandonedAnimal animal = getAnimal(animalId);

        // 지원서 생성
        TemporaryProtection temporaryProtection = new TemporaryProtectionBuilder(member, animal)
                .setLocation(tempProtectionDto.getLocation())
                .setJob(tempProtectionDto.getJob())
                .setMarried(tempProtectionDto.isMarried())
                .setPeriod(tempProtectionDto.getPeriod())
                .build();

        // 지원서 저장
        applicationRepository.save(temporaryProtection);

        return temporaryProtection.getId();
    }

    // 후원하기
    @Transactional
    public Long donate(Long memberId, Long animalId, ApplyDonationForm donationDto) {
        // 엔티티 조회
        Member member = getMember(memberId);
        AbandonedAnimal animal = getAnimal(animalId);

        // 지원서 생성
        Donation donation = new DonationBuilder(member, animal)
                .setBankType(donationDto.getBankType())
                .setDonator(donationDto.getDonator())
                .setAccountNumber(donationDto.getAccountNumber())
                .setAmount(donationDto.getAmount())
                .build();

        // 지원서 저장
        applicationRepository.save(donation);

        return donation.getId();
    }

    private AbandonedAnimal getAnimal(Long animalId) {
        return animalRepository.findById(animalId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    // 지원서 조회
    public <T extends Application> Optional<T> findOne(Long id, Class<T> type) {
        return applicationRepository.findById(id)
                .map(application -> (T) application)
                .stream()
                .findAny();
    }

    // 전체 지원서 조회
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    // 특정 회원의 전체 지원서 조회
    public List<Application> findAll(Member member) {
        return applicationRepository.findAllByMemberOrderByApplicationType(member);
    }

    // 입양 정보 수정
    @Transactional
    public Long modifyAdopt(Long id, ModifyAdoptForm adoptDto) {
        Adopt adopt = findOne(id, Adopt.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        adopt.changeInfo(adoptDto);

        return adopt.getId();
    }

    // 임시보호 정보 수정
    @Transactional
    public Long modifyTempProtection(Long id, ModifyTempProtectionForm tempProtectionDto) {
        TemporaryProtection temporaryProtection = findOne(id, TemporaryProtection.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        temporaryProtection.changeInfo(tempProtectionDto);

        return temporaryProtection.getId();
    }

    // 후원 정보 수정
    @Transactional
    public Long modifyDonation(Long id, ModifyDonationForm donationDto) {
        Donation donation = findOne(id, Donation.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        donation.changeInfo(donationDto);

        return donation.getId();
    }

    // 지원서 삭제
    @Transactional
    public void deleteApplication(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        application.getAbandonedAnimal()
                .getApplications()
                .removeIf(app -> app.getId().equals(id));

        application.getMember()
                .getApplications()
                .removeIf(app -> app.getId().equals(id));

        applicationRepository.deleteById(id);
    }
}

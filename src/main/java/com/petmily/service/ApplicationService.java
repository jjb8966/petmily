package com.petmily.service;

import com.petmily.builder.application.AdoptBuilder;
import com.petmily.builder.application.DonationBuilder;
import com.petmily.builder.application.TemporaryProtectionBuilder;
import com.petmily.domain.AbandonedAnimal;
import com.petmily.domain.Member;
import com.petmily.domain.application.Adopt;
import com.petmily.domain.application.Application;
import com.petmily.domain.application.Donation;
import com.petmily.domain.application.TemporaryProtection;
import com.petmily.dto.application.*;
import com.petmily.repository.AbandonedAnimalRepository;
import com.petmily.repository.ApplicationRepository;
import com.petmily.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final MemberRepository memberRepository;
    private final AbandonedAnimalRepository animalRepository;

    // 입양하기
    public Long adopt(Long memberId, Long animalId, ApplyAdoptDto adoptDto) {
        // 엔티티 조회
        Member member = getMember(memberId);
        AbandonedAnimal animal = getAnimal(animalId);

        // 지원서 생성
        Adopt adopt = new AdoptBuilder(member, animal)
                .setAddress(adoptDto.getAddress())
                .setJob(adoptDto.getJob())
                .setMarried(adoptDto.getMarried())
                .build();

        // 지원서 저장
        applicationRepository.save(adopt);

        return adopt.getId();
    }

    // 임시보호하기
    public Long tempProtect(Long memberId, Long animalId, ApplyTempProtectionDto tempProtectionDto) {
        // 엔티티 조회
        Member member = getMember(memberId);
        AbandonedAnimal animal = getAnimal(animalId);

        // 지원서 생성
        TemporaryProtection temporaryProtection = new TemporaryProtectionBuilder(member, animal)
                .setPeriod(tempProtectionDto.getPeriod())
                .build();

        // 지원서 저장
        applicationRepository.save(temporaryProtection);

        return temporaryProtection.getId();
    }

    // 후원하기
    public Long donate(Long memberId, Long animalId, ApplyDonationDto donationDto) {
        // 엔티티 조회
        Member member = getMember(memberId);
        AbandonedAnimal animal = getAnimal(animalId);

        // 지원서 생성
        Donation donation = new DonationBuilder(member, animal)
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

    // 입양 정보 수정
    public Long changeAdoptInfo(Long id, ChangeAdoptDto adoptDto) {
        Adopt adopt = findOne(id, Adopt.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        adopt.changeInfo(adoptDto);

        return adopt.getId();
    }

    // 임시보호 정보 수정
    public Long changeTempProtectionInfo(Long id, ChangeTempProtectionDto tempProtectionDto) {
        TemporaryProtection temporaryProtection = findOne(id, TemporaryProtection.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        temporaryProtection.changeInfo(tempProtectionDto);

        return temporaryProtection.getId();
    }

    // 후원 정보 수정
    public Long changeDonationInfo(Long id, ChangeDonationDto donationDto) {
        Donation donation = findOne(id, Donation.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        donation.changeInfo(donationDto);

        return donation.getId();
    }

    // 지원서 삭제
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

package com.petmily.service;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.dto.abandoned_animal.ChangeAnimalDto;
import com.petmily.repository.AbandonedAnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AbandonedAnimalService {

    private final AbandonedAnimalRepository animalRepository;
    private final ApplicationService applicationService;

    // 유기동물 등록
    public Long register(AbandonedAnimal abandonedAnimal) {
        animalRepository.save(abandonedAnimal);

        return abandonedAnimal.getId();
    }

    // 유기동물 조회
    public Optional<AbandonedAnimal> findOne(Long id) {
        return animalRepository.findById(id);
    }

    // 전체 유기동물 조회
    public List<AbandonedAnimal> findAll() {
        return animalRepository.findAll();
    }

    // 유기동물 정보 변경
    public Long changeAnimalInfo(Long id, ChangeAnimalDto animalDto) {
        AbandonedAnimal abandonedAnimal = animalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        abandonedAnimal.changeInfo(animalDto);

        return abandonedAnimal.getId();
    }

    // 유기동물 삭제
    public void deleteAnimal(Long id) {
        AbandonedAnimal animal = animalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        deleteApplicationsAboutAnimal(id);

        animalRepository.deleteById(id);
    }

    private void deleteApplicationsAboutAnimal(Long animalId) {
        applicationService.findAll().stream()
                .filter(application -> application.getAbandonedAnimal().getId().equals(animalId))
                .forEach(application -> applicationService.deleteApplication(application.getId()));
    }
}

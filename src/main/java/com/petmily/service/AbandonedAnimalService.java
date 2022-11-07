package com.petmily.service;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.dto.PetmilyPage;
import com.petmily.domain.dto.abandoned_animal.AnimalDetailForm;
import com.petmily.domain.dto.abandoned_animal.ChangeAnimalDto;
import com.petmily.repository.AbandonedAnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public AbandonedAnimal findOne(Long id) {
        AbandonedAnimal animal = animalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        return animal;
    }

    // 전체 유기동물 조회
    public List<AbandonedAnimal> findAll() {
        return animalRepository.findAll();
    }

    // 전체 유기동물 조회 (페이지)
    public Page<AbandonedAnimal> findAll(Pageable pageable) {
        return animalRepository.findAll(pageable);
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

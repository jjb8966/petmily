package com.petmily.service;

import com.petmily.domain.AbandonedAnimal;
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

    private final AbandonedAnimalRepository repository;

    // 유기동물 등록
    public Long register(AbandonedAnimal abandonedAnimal) {
        repository.save(abandonedAnimal);

        return abandonedAnimal.getId();
    }

    // 유기동물 조회
    public Optional<AbandonedAnimal> findOne(Long id) {
        return repository.findById(id);
    }

    // 전체 유기동물 조회
    public List<AbandonedAnimal> findAll() {
        return repository.findAll();
    }

    // 유기동물 정보 변경
    public Long changeAnimalInfo(Long id, ChangeAnimalDto animalDto) {
        AbandonedAnimal abandonedAnimal = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        abandonedAnimal.changeInfo(animalDto);

        return abandonedAnimal.getId();
    }

    // 유기동물 삭제
    public void deleteAnimal(Long id) {
        repository.deleteById(id);
    }
}

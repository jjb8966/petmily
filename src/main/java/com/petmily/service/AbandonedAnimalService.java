package com.petmily.service;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.dto.abandoned_animal.ChangeAnimalForm;
import com.petmily.domain.dto.abandoned_animal.ModifyAnimalApiForm;
import com.petmily.repository.AbandonedAnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AbandonedAnimalService {

    private final AbandonedAnimalRepository animalRepository;
    private final MessageSource ms;

    // 유기동물 등록
    @Transactional
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

    // 전체 유기동물 조회 (페이지)
    public Page<AbandonedAnimal> findAll(Pageable pageable) {
        return animalRepository.findAll(pageable);
    }

    // 유기동물 정보 변경
    @Transactional
    public Long changeAnimalInfo(Long id, ChangeAnimalForm animalDto) {
        AbandonedAnimal abandonedAnimal = animalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.animal.null")));

        abandonedAnimal.changeInfo(animalDto);

        return abandonedAnimal.getId();
    }

    // 유기동물 삭제
    @Transactional
    public void deleteAnimal(Long id) {
        animalRepository.deleteById(id);
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }

    public void modify(Long animalId, ModifyAnimalApiForm form) {

    }
}

package com.petmily.repository;

import com.petmily.domain.AbandonedAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbandonedAnimalRepository extends JpaRepository<AbandonedAnimal, Long> {
}

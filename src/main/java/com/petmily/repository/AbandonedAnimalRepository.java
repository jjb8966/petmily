package com.petmily.repository;

import com.petmily.domain.core.AbandonedAnimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AbandonedAnimalRepository extends JpaRepository<AbandonedAnimal, Long> {

    Page<AbandonedAnimal> findAll(Pageable pageable);

    @Query("select a.name from AbandonedAnimal a where a.id = :id")
    String findName(@Param("id") Long id);
}

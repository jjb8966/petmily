package com.petmily.domain.core;

import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.dto.abandoned_animal.ModifyAnimalApiForm;
import com.petmily.domain.dto.abandoned_animal.ModifyAnimalForm;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@ToString(of = {"name", "kind", "age", "weight", "species", "status"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AbandonedAnimal extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "abandonedAnimal_id")
    private Long id;

    @OneToMany(mappedBy = "abandonedAnimal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;

    @OneToOne(mappedBy = "abandonedAnimal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Picture picture;

    @Enumerated(EnumType.STRING)
    private AnimalSpecies species;

    @Enumerated(EnumType.STRING)
    private AnimalStatus status;

    private String name;
    private String kind;
    private Integer age;
    private Float weight;

    public AbandonedAnimal(AbandonedAnimalBuilder builder) {
        this.applications = builder.getApplies();
        this.picture = builder.getPicture();
        this.species = builder.getSpecies();
        this.status = builder.getStatus();
        this.name = builder.getName();
        this.kind = builder.getKind();
        this.age = builder.getAge();
        this.weight = builder.getWeight();
    }

    public void changeInfo(ModifyAnimalForm animalDto) {
        this.picture = animalDto.getPicture();
        this.species = animalDto.getSpecies();
        this.status = animalDto.getStatus();
        this.name = animalDto.getName();
        this.kind = animalDto.getKind();
        this.age = animalDto.getAge();
        this.weight = animalDto.getWeight();
    }

    public void changeInfo(ModifyAnimalApiForm apiForm) {
        this.species = AnimalSpecies.valueOf(apiForm.getSpecies());
        this.status = AnimalStatus.valueOf(apiForm.getStatus());
        this.name = apiForm.getName();
        this.kind = apiForm.getKind();
        this.age = apiForm.getAge();
        this.weight = apiForm.getWeight();
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}

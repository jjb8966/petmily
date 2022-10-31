package com.petmily.domain;

import com.petmily.domain.application.Application;
import com.petmily.builder.AbandonedAnimalBuilder;
import com.petmily.dto.abandoned_animal.ChangeAnimalDto;
import com.petmily.enum_type.AnimalSpecies;
import com.petmily.enum_type.AnimalStatus;
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
public class AbandonedAnimal {

    @Id
    @GeneratedValue
    @Column(name = "abandonedAnimal_id")
    private Long id;

    @OneToMany(mappedBy = "abandonedAnimal", cascade = CascadeType.ALL)
    private List<Application> applications;

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
        this.species = builder.getSpecies();
        this.status = builder.getStatus();
        this.name = builder.getName();
        this.kind = builder.getKind();
        this.age = builder.getAge();
        this.weight = builder.getWeight();
    }

    public void changeInfo(ChangeAnimalDto animalDto) {
        this.species = animalDto.getSpecies();
        this.name = animalDto.getName();
        this.kind = animalDto.getKind();
        this.age = animalDto.getAge();
        this.weight = animalDto.getWeight();
    }
}

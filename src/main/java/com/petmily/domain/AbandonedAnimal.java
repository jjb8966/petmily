package com.petmily.domain;

import com.petmily.domain.embedded_type.Picture;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class AbandonedAnimal {

    @Id @GeneratedValue
    @Column(name = "abandonedAnimal_id")
    private Long id;

    @OneToMany(mappedBy = "abandonedAnimal", cascade = CascadeType.ALL)
    private List<Application> applications;

    @ElementCollection
    private List<Picture> pictures;

    @Enumerated(EnumType.STRING)
    private AnimalSpecies species;

    @Enumerated(EnumType.STRING)
    private AnimalStatus status;

    private String name;
    private String kind;
    private Integer age;
    private Float weight;

}

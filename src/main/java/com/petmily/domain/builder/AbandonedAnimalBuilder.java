package com.petmily.domain.builder;

import com.petmily.domain.AbandonedAnimal;
import com.petmily.domain.Application;
import com.petmily.domain.embedded_type.Picture;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AbandonedAnimalBuilder {

    private List<Application> applications = new ArrayList<>();
    private List<Picture> pictures = new ArrayList<>();
    private AnimalSpecies species;
    private AnimalStatus status = AnimalStatus.PROTECTED;
    private String name;
    private String kind;
    private Integer age;
    private Float weight;

    public AbandonedAnimal build() {
        return new AbandonedAnimal(this);
    }

    public AbandonedAnimalBuilder setApplications(List<Application> applications) {
        this.applications = applications;
        return this;
    }

    public AbandonedAnimalBuilder setPictures(List<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }

    public AbandonedAnimalBuilder setSpecies(AnimalSpecies species) {
        this.species = species;
        return this;
    }

    public AbandonedAnimalBuilder setStatus(AnimalStatus status) {
        this.status = status;
        return this;
    }

    public AbandonedAnimalBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public AbandonedAnimalBuilder setKind(String kind) {
        this.kind = kind;
        return this;
    }

    public AbandonedAnimalBuilder setAge(int age) {
        this.age = age;
        return this;
    }

    public AbandonedAnimalBuilder setWeight(float weight) {
        this.weight = weight;
        return this;
    }
}

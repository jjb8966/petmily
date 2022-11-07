package com.petmily.domain.builder;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Picture;
import lombok.Getter;

@Getter
public class PictureBuilder {

    private AbandonedAnimal abandonedAnimal;
    private String fileStoreName;

    public Picture build() {
        Picture picture = new Picture(this);
        abandonedAnimal.setPicture(picture);

        return picture;
    }

    public PictureBuilder(AbandonedAnimal abandonedAnimal, String fileStoreName) {
        this.abandonedAnimal = abandonedAnimal;
        this.fileStoreName = fileStoreName;
    }
}

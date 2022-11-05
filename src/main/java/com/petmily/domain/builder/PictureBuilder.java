package com.petmily.domain.builder;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Picture;
import lombok.Getter;

import java.util.List;

@Getter
public class PictureBuilder {

    private AbandonedAnimal abandonedAnimal;
    private String fileStoreName;

    public Picture build() {
        Picture picture = new Picture(this);
        abandonedAnimal.getPictures().add(picture);

        return picture;
    }

    public PictureBuilder(AbandonedAnimal abandonedAnimal, String fileStoreName) {
        this.abandonedAnimal = abandonedAnimal;
        this.fileStoreName = fileStoreName;
    }
}

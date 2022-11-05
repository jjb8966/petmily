package com.petmily.domain.core;

import com.petmily.domain.builder.PictureBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Picture {

    @Id @GeneratedValue
    @Column(name = "picture_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abandonedAnimal_id")
    private AbandonedAnimal abandonedAnimal;

    private String fileStoreName;

    public Picture(PictureBuilder builder) {
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.fileStoreName = builder.getFileStoreName();
    }

    @Override
    public String toString() {
        return "Picture{" +
                "abandonedAnimal=" + abandonedAnimal.getId() +
                ", fileStoreName='" + fileStoreName + '\'' +
                '}';
    }
}

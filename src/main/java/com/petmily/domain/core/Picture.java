package com.petmily.domain.core;

import com.petmily.domain.builder.PictureBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Picture extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "picture_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abandonedAnimal_id")
    private AbandonedAnimal abandonedAnimal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String fileUploadName;
    private String fileStoreName;

    public Picture(PictureBuilder builder) {
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.board = builder.getBoard();
        this.fileUploadName =
                this.fileStoreName = builder.getFileStoreName();
    }

    public void deleteBoard() {
        board = null;
    }
}

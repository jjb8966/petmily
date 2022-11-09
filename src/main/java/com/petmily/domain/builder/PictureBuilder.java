package com.petmily.domain.builder;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Picture;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Getter
@Slf4j
public class PictureBuilder {

    private AbandonedAnimal abandonedAnimal;
    private Board board;
    private String fileUploadName;
    private String fileStoreName;

    public Picture build() {
        Picture picture = new Picture(this);

        if (abandonedAnimal != null) {
            abandonedAnimal.setPicture(picture);
        }

        if (board != null) {
            board.getPictures().add(picture);
        }

        return picture;
    }

    public PictureBuilder setAbandonedAnimal(AbandonedAnimal abandonedAnimal) {
        this.abandonedAnimal = abandonedAnimal;
        return this;
    }

    public PictureBuilder setBoard(Board board) {
        this.board = board;
        return this;
    }

    public PictureBuilder setFileUploadName(String fileUploadName) {
        this.fileUploadName = fileUploadName;
        return this;
    }

    public PictureBuilder setFileStoreName(String fileStoreName) {
        this.fileStoreName = fileStoreName;
        return this;
    }

    public PictureBuilder store(MultipartFile multipartFile, String storePath) {
        fileUploadName = multipartFile.getOriginalFilename();
        fileStoreName = changeToStoreName(fileUploadName);

        log.info("upload name = {}", fileUploadName);
        log.info("store name = {}", fileStoreName);
        log.info("storePath = {}", storePath);

        try {
            log.info("full path = {}", storePath + fileStoreName);
            multipartFile.transferTo(new File(storePath + fileStoreName));
            log.info("사진 저장 성공");
        } catch (IOException e) {
            throw new RuntimeException("사진 저장 실패", e);
        }

        return this;
    }

    private String changeToStoreName(String fileUploadName) {
        int position = fileUploadName.lastIndexOf(".");
        String extension = fileUploadName.substring(position + 1);

        return UUID.randomUUID() + "." + extension;
    }
}

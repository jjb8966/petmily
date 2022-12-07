package com.petmily.service;

import com.petmily.domain.builder.PictureBuilder;
import com.petmily.domain.core.Picture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Slf4j
class PictureServiceTest {

    @Value("${file.dir}")
    String storePath;

    @Autowired
    EntityManager em;

    @Autowired
    PictureService pictureService;

    @AfterEach
    void afterEach() throws Exception {
        URL testImageURL = new URL("file:/Users/joojongbum/picture/test.png");
        BufferedImage testImage = ImageIO.read(testImageURL);

        ImageIO.write(testImage, "png", new File("/Users/joojongbum/Desktop/work/project/petmily/src/test/resources/static/picture/test.png"));
    }

    @Test
    @DisplayName("게시글을 삭제하면 서버에 저장된 게시글 관련 사진 데이터가 삭제된다.")
    void delete() throws IOException {
        //given
        Picture picture = new PictureBuilder()
                .setFileStoreName("test.png")
                .build();

        String fullPath = "file:" + storePath + picture.getFileStoreName();

        UrlResource file = new UrlResource(fullPath);

        assertThat(file).isNotNull();

        //when
        pictureService.delete(picture);

        URL deletedFile = new URL(fullPath);

        //then
        assertThatThrownBy(() -> deletedFile.getContent())
                .isInstanceOf(FileNotFoundException.class)
                .as("사진이 삭제되어 찾을 수 없음");
    }
}
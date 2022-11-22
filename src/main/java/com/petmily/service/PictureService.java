package com.petmily.service;

import com.petmily.domain.builder.PictureBuilder;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Picture;
import com.petmily.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PictureService {

    @Value("${file.dir}")
    private String storePath;

    private final PictureRepository pictureRepository;

    public UrlResource findOne(String fileStoreName) throws MalformedURLException {
        log.info("fileStoreName = {}", fileStoreName);
        String fullPath = "file:" + storePath + fileStoreName;
        log.info("full path = {}", fullPath);

        return new UrlResource(fullPath);
    }

    @Transactional
    public Long store(MultipartFile multipartFile, Board board) {
        Picture picture = new PictureBuilder()
                .setBoard(board)
                .store(multipartFile, storePath)
                .build();

        pictureRepository.save(picture);

        return picture.getId();
    }

    @Transactional
    public void store(List<MultipartFile> multipartFiles, Board board) {
        multipartFiles.stream()
                .forEach(multipartFile -> store(multipartFile, board));

    }
}

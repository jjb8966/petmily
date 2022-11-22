package com.petmily.service;

import com.petmily.domain.builder.PictureBuilder;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Picture;
import com.petmily.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PictureService {

    @Value("${file.dir}")
    private String storePath;

    private final PictureRepository pictureRepository;

    public Long store(MultipartFile multipartFile, Board board) {
        Picture picture = new PictureBuilder()
                .setBoard(board)
                .store(multipartFile, storePath)
                .build();

        pictureRepository.save(picture);

        return picture.getId();
    }

    public void store(List<MultipartFile> multipartFiles, Board board) {
        multipartFiles.stream()
                .forEach(multipartFile -> store(multipartFile, board));

    }
}

package com.petmily.domain.dto.board;

import com.petmily.domain.enum_type.AnimalSpecies;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class WriteBoardForm {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @Nullable
    private Boolean shownAll = true;

    @Nullable
    private List<MultipartFile> pictures;

    // 찾아요/봤어요 게시글 전용 필드
    @Nullable
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime lostOrWatchTime;

    @Nullable
    private AnimalSpecies species;

    @Nullable
    private String animalName;

    @Nullable
    private String animalKind;

    @Nullable
    private Integer animalAge;

    @Nullable
    private Float animalWeight;
}

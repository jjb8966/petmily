package com.petmily.domain.dto.board;

import com.petmily.domain.core.enum_type.BoardType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
public class WriteBoardForm {

    private List<MultipartFile> pictures;

    @NotNull
    private BoardType boardType;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private Boolean shownAll = true;
}

package com.petmily.dto.board;

import com.petmily.domain.Reply;
import com.petmily.embedded_type.Picture;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class WriteBoardDto {

    private List<Reply> replies = new ArrayList<>();
    private List<Picture> pictures = new ArrayList<>();
    private String title;
    private String content;
    private boolean shownAll = true;
}

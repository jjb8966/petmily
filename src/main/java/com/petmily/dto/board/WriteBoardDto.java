package com.petmily.dto.board;

import com.petmily.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class WriteBoardDto {

    private List<Reply> replies = new ArrayList<>();
    private String title;
    private String content;
    private boolean shownAll = true;
}

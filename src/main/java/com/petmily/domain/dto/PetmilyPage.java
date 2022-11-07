package com.petmily.domain.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PetmilyPage<T> {

    public static final Integer NUMBER_OF_PAGES_TO_SHOW = 5;

    private Page<T> realPage;
    private Integer startPage;
    private Integer endPage;

    public PetmilyPage(Page<T> realPage) {
        this.realPage = realPage;

        startPage = (realPage.getNumber() / NUMBER_OF_PAGES_TO_SHOW) * NUMBER_OF_PAGES_TO_SHOW + 1;
        endPage = startPage + 4;
    }
}

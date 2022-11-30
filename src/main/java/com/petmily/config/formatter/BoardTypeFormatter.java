package com.petmily.config.formatter;

import com.petmily.domain.enum_type.BoardType;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class BoardTypeFormatter implements Formatter<BoardType> {

    @Override
    public BoardType parse(String text, Locale locale) throws ParseException {
        return BoardType.valueOf(text.toUpperCase());
    }

    @Override
    public String print(BoardType object, Locale locale) {
        return object.name().toLowerCase();
    }
}

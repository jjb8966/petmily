package com.petmily.config.formatter;

import com.petmily.domain.enum_type.AnimalSpecies;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public class AnimalSpeciesFormatter implements Formatter<AnimalSpecies> {

    @Override
    public AnimalSpecies parse(String text, Locale locale) throws ParseException {
        return AnimalSpecies.valueOf(text.toUpperCase());
    }

    @Override
    public String print(AnimalSpecies object, Locale locale) {
        return object.getDescription().toLowerCase();
    }
}

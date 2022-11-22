package com.petmily.domain.dto_converter;

import com.petmily.domain.core.BaseEntity;

import java.util.Optional;

public interface EntityDtoConverter {

    public <T> Optional<T> entityToDto(BaseEntity entity, Class<T> dtoType);
}

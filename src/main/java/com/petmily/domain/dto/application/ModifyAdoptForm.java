package com.petmily.domain.dto.application;

import com.petmily.domain.core.enum_type.LocationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyAdoptForm {

    private LocationType location;
    private String job;
    private Boolean married;
}

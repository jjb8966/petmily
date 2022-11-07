package com.petmily.domain.dto.application;

import com.petmily.domain.core.enum_type.LocationType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ApplyTempProtectionDto {

    @NotNull
    private LocationType location;

    @NotBlank
    private String job;

    @NotNull
    private boolean married;

    @NotNull
    @Range(min = 1L, max = 6L)
    private Integer period;

}

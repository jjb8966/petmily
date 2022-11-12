package com.petmily.domain.dto.application;

import com.petmily.domain.core.enum_type.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ApplicationListForm {

    @NotNull
    private Long id;

    @NotBlank
    private String animalName;

    @NotBlank
    private String type;

    @NotNull
    private ApplicationStatus status;
}

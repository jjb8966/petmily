package com.petmily.domain.dto.application;

import com.petmily.domain.enum_type.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApplicationListForm {

    private Long id;
    private String animalName;
    private String type;
    private ApplicationStatus status;
    private String applicantName;
}

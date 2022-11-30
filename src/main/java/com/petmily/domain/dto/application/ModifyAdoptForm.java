package com.petmily.domain.dto.application;

import com.petmily.domain.core.enum_type.LocationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ModifyAdoptForm {

    private LocationType location;
    private String job;
    private Boolean married;

    public ModifyAdoptForm(AdoptDetailForm form) {
        this.location = form.getLocation();
        this.job = form.getJob();
        this.married = form.getMarried();
    }
}
